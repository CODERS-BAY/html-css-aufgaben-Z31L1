<?php
        include('login.inc.php');

$story_id = $_REQUEST['target_id'];

$sql      = "SELECT * FROM story WHERE id = " . $story_id;
$result   = $con->query($sql);
$output   = '';

if($result->num_rows > 0) {
   while($row = $result->fetch_assoc()) {

        $output .= '<section><img  src="img/' . $row['img'] . '"></section><section id="text"><p>' . $row['storytext'] . '</p></section>' ;
        $targetID = $row['id'];
        $subsql = "SELECT subtext,target_id FROM substory WHERE
        story_id = " . $targetID;
        
        $subresult = $con->query($subsql);
        if($subresult->num_rows > 0) {
            while ($subrow = $subresult->fetch_assoc()) {
                $output .= '<section id="multichoice"><button type = "button" class= "btn"
                onclick = "story(' . $subrow['target_id'] . ')"><p>' . $subrow['subtext'] .'</p></button></section>';
            }
        }
   }
   echo $output;

}else {
    echo "nicht gefunden in der Datenbank";
}

$con->close();

?>