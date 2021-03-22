<!doctype html>
<html>
    <head>
        <?php
        include ('login.inc.php');
        ?>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.0-beta2/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-BmbxuPwQa2lc/FVzBcNJ7UAyJxM6wuqIj61tLrc4wSX0szH/Ev+nYRRuWlolflfl" crossorigin="anonymous">
        <meta charset="UTF-8">
    </head>
    <body>
    <main class="container m-5">
        
        <form method="post" action="save.php">
            <input type="hidden" name="forStory" value="mainstory">
            <div class="mb-3">
                <label for="story" class="form-label">Story Headline</label>
                <input type="text" class="form-control" name="mainstory" id="story">
            </div>
                        <div class="mb-3">
                <label for="story" class="form-label">img</label>
                <input type="text" class="form-control" name="img-upload" id="story">
            </div>
            <button type="submit" class="btn btn-success">Save</button>
        </form>

        
        <form method="post" action="save.php">
            <input type="hidden" name="forStory" value="substory">
            <div class="mb-3">
                <label for="story" class="form-label">Story Headline</label>
                <input type="text" class="form-control" name="substory" id="story">
            <input type="hidden" name="forimg" value="img">
            

        <div class="mb-3">
            <label for="sotry_id" class="form-label">Woher komme ich</label>
            <select class="form-select row " name="story_from">
                <option selected>Open this select menu</option>
                <?php
                    $sql = "SELECT * FROM story";
                    $result = $con->query($sql);
                    if($result->num_rows > 0 ){
                        while($row = $result->fetch_assoc()){
                            ?>
                            <option class="col-8 text-truncate" value="<?php echo $row['id']; ?>"><?php echo $row['storytext']?></option>
                            <?php
                        }
                    }
                ?>
            </select>
        </div>

        <div class="mb-3">
            <label for="sotry_id" class="form-label">Wohin gehe ich</label>
            <select class="form-select row" name="story_to">
                <option selected>Open this select menu</option>

                <?php
                    $sql = "SELECT * FROM story";
                    $result = $con->query($sql);
                    if($result->num_rows > 0 ){
                        while($row = $result->fetch_assoc()){
                            ?>
                            <option class="col-8 text-truncate" value="<?php echo $row['id']; ?>"><?php echo $row['storytext']?></option>
                            <?php
                        }
                    }
                ?>
            </select>
        </div>

        <button type="submit" class="btn btn-success">Save</button>
        </form>


        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.0-beta2/dist/js/bootstrap.bundle.min.js" integrity="sha384-b5kHyXgcpbZJO/tY9Ul7kGkf1S0CWuKcCD38l8YkeH8z8QjE0GmW1gYU5S9FOnJ0" crossorigin="anonymous"></script>

        </main>
    </body>
</html>