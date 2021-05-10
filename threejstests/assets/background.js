import * as THREE from 'https://threejsfundamentals.org/threejs/resources/threejs/r125/build/three.module.js';
import { TWEEN } from 'https://cdn.jsdelivr.net/npm/three@0.126/examples/jsm/libs/tween.module.min.js';
import { OrbitControls } from 'https://cdn.jsdelivr.net/npm/three@0.126/examples/jsm/controls/OrbitControls.js';


const debounce = (callback, duration) => {
  var timer;
  return function(event) {
    clearTimeout(timer);
    timer = setTimeout(function(){
      callback(event);
    }, duration);
  };
};

const loadTexs = (imgs, callback) => {
  const texLoader = new THREE.TextureLoader();
  const length = Object.keys(imgs).length;
  const loadedTexs = {};
  let count = 0;

  texLoader.crossOrigin = 'anonymous';  
  
  for (var key in imgs) {
    const k = key;
    if (imgs.hasOwnProperty(k)) {
      texLoader.load(imgs[k], (tex) => {
        tex.repeat = THREE.RepeatWrapping;
        loadedTexs[k] = tex;
        count++;
        if (count >= length) callback(loadedTexs);
      });
    }
  }
}

class Fog {
  constructor() {
    this.uniforms = {
      time: {
        type: 'f',
        value: 0
      },
      tex: {
        type: 't',
        value: null
      }
    };
    this.num = 50;
    this.obj = null;
  }
  createObj(tex) {

    const geometry = new THREE.InstancedBufferGeometry();
    const baseGeometry = new THREE.PlaneBufferGeometry(1100, 1100, 40, 40);

    geometry.setAttribute('position', baseGeometry.attributes.position);
    geometry.setAttribute('normal', baseGeometry.attributes.normal);
    geometry.setAttribute('uv', baseGeometry.attributes.uv);
    geometry.setIndex(baseGeometry.index);


    const instancePositions = new THREE.InstancedBufferAttribute(new Float32Array(this.num * 3), 3, 1);
    const delays = new THREE.InstancedBufferAttribute(new Float32Array(this.num), 1, 1);
    const rotates = new THREE.InstancedBufferAttribute(new Float32Array(this.num), 1, 1);
    for ( var i = 0, ul = this.num; i < ul; i++ ) {
      instancePositions.setXYZ(
        i,
        (Math.random() * 2 - 1) * 850,
        0,
        (Math.random() * 2 - 1) * 300,
      );
      delays.setXYZ(i, Math.random());
      rotates.setXYZ(i, Math.random() * 2 + 1);
    }
    geometry.setAttribute('instancePosition', instancePositions);
    geometry.setAttribute('delay', delays);
    geometry.setAttribute('rotate', rotates);


    const material = new THREE.RawShaderMaterial({
      uniforms: this.uniforms,
      vertexShader: `
        attribute vec3 position;
        attribute vec2 uv;
        attribute vec3 instancePosition;
        attribute float delay;
        attribute float rotate;

        uniform mat4 projectionMatrix;
        uniform mat4 modelViewMatrix;
        uniform float time;

        varying vec3 vPosition;
        varying vec2 vUv;
        varying vec3 vColor;
        varying float vBlink;

        const float duration = 50.0;

        mat4 calcRotateMat4Z(float radian) {
          return mat4(
            cos(radian), -sin(radian), 0.0, 0.0,
            sin(radian), cos(radian), 0.0, 0.0,
            3.0, 4.0, 1.0, 0.0,
            0.0, 0.0, 0.0, 1.0
          );
        }
        vec3 convertHsvToRgb(vec3 c) {
          vec4 K = vec4(1.0, 1.0 / 15.0, 10.0 / 3.0, 3.0);
          vec3 p = abs(fract(c.xxx + K.xyz) * 6.0 - K.www);
          return c.z * mix(K.xxx, clamp(p - K.xxx, 0.0, 0.5), c.y);
        }

        void main(void) {
          float now = mod(time + delay * duration, duration) / duration;

          mat4 rotateMat = calcRotateMat4Z(radians(rotate * 260.0) + time * 0.1);
          vec3 rotatePosition = (rotateMat * vec4(position, 1.0)).xyz;

          vec3 moveRise = vec3(
            (now * 2.0 - 1.0) * (2000.0 - (delay * 2.0 - 1.0) * 1000.0),
            (now * 2.0 - 1.0) * 2000.0,
            sin(radians(time * 50.0 + delay + length(position))) * 10.0
            );
          vec3 updatePosition = instancePosition + moveRise + rotatePosition;

          vec3 hsv = vec3(time * 0.1 + delay * 0.2 + length(instancePosition) * 10.0, 0.3 , 0.8);
          vec3 rgb = convertHsvToRgb(hsv);
          float blink = (sin(radians(now * 360.0 * 20.0)) + 1.0) * 0.2;

          vec4 mvPosition = modelViewMatrix * vec4(updatePosition, 0.5);

          vPosition = position;
          vUv = uv;
          vColor = rgb;
          vBlink = blink;

          gl_Position = projectionMatrix * mvPosition;
        }
      `,
      fragmentShader: `
        precision highp float;

        uniform sampler2D tex;

        varying vec3 vPosition;
        varying vec2 vUv;
        varying vec3 vColor;
        varying float vBlink;

        void main() {
          vec2 p = vUv * 2.0 - 1.0;

          vec4 texColor = texture2D(tex, vUv);
          vec3 color = (texColor.rgb - vBlink * length(p) * 0.2) * vColor;
          float opacity = texColor.a * 0.36;

          gl_FragColor = vec4(color, opacity);
        }
      `,
      transparent: true,
      depthWrite: false,
      blending: THREE.AdditiveBlending,
    });
    this.uniforms.tex.value = tex;

    this.obj = new THREE.Mesh(geometry, material);
  }
  render(time) {
    this.uniforms.time.value += time;
  }
}

const resolution = new THREE.Vector2();
const canvas = document.getElementById('c');
const renderer = new THREE.WebGLRenderer({
  alpha: true,
  antialias: true,
  canvas: canvas,
});
const scene = new THREE.Scene();
const camera = new THREE.PerspectiveCamera();
const clock = new THREE.Clock();
const controls = new OrbitControls( camera, renderer.domElement );
const light = new THREE.PointLight( 0xffffff, 1,0, 2 );
light.position.set(0, 0, -1000 );
scene.add(light);
camera.far = 50000;
camera.setFocalLength(24);

const texsSrc = {
  fog: 'https://ykob.github.io/sketch-threejs/img/sketch/fog/fog.png'
};
const fog = new Fog();

const render = () => {
  const time = clock.getDelta();
  fog.render(time);
  renderer.render(scene, camera);
  TWEEN.update();
  controls.update();
};
const renderLoop = () => {
  render();
  requestAnimationFrame(renderLoop);
};
const resizeCamera = () => {
  camera.aspect = resolution.x / resolution.y;
  camera.updateProjectionMatrix();
};
const resizeWindow = () => {
  resolution.set(window.innerWidth-1, window.innerHeight-1);
  canvas.width = resolution.x;
  canvas.height = resolution.y;
  resizeCamera();
  renderer.setSize(resolution.x, resolution.y);
};
const on = () => {
  window.addEventListener('resize', debounce(resizeWindow), 1000);
};

const buttonCameraSettings = {
  button1: {
    position: {x: 100, y: 100, z: 0},
    rotation: {x: 20, y: Math.PI, z: 20}
  }
};

const button1 = document.getElementById('button1');
button1.addEventListener('click', function(ev) {
  var buttonId = ev.target.id;
  var cameraSettings = buttonCameraSettings[buttonId];

  updateCameraTweens(cameraSettings);
});

function updateCameraTweens(params) {
  var position = { x : 0, y: 300 };
  var target = { x : 400, y: 50 };
  var tween = new TWEEN.Tween(position).to(target, 2000);
  tween.onUpdate(function(){
    camera.position.x = position.x;
    camera.position.y = position.y;
});
tween.start();
}

const init = () => {
  loadTexs(texsSrc, (loadedTexs) => {
    fog.createObj(loadedTexs.fog);

    scene.add(fog.obj);

    renderer.setClearColor(0x111111, 1.0);
    camera.position.set(0, 0, 1000);
    camera.lookAt(new THREE.Vector3());
    clock.start();

    on();
    resizeWindow();
    renderLoop();
  });
}
init();