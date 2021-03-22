<?php
include('login.inc.php');

if($_REQUEST['forStory'] == 'mainstory'){
    $stmt = $con->prepare('INSERT INTO story(storytext, img) VALUE (?,?)');
    $stmt->bind_param('ss', $story,$img);

    $story = $_REQUEST['mainstory'];
    $img = $_REQUEST['img-upload'];
    if($stmt->execute()){
        header('Location: ' . $_SERVER['HTTP_REFERER']);
    } else {
        print "FEHLER1";
    }
}

if($_REQUEST['forStory'] == 'substory'){
    $stmt = $con->prepare('INSERT INTO substory (story_id, subtext, target_id) VALUE (?,?,?)');
    $stmt->bind_param('isi', $story_id, $subtext, $target_id);

    $story_id = $_REQUEST['story_from'];
    $subtext = $_REQUEST['substory'];
    $target_id = $_REQUEST['story_to'];
    

    if ($stmt->execute()) {
        header('Location: ' . $_SERVER['HTTP_REFERER']);
    }else {
        print "FEHLER2";
    }

}

?>