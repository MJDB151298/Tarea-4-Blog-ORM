<!DOCTYPE html>
<html lang="en">

<head>

  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
  <meta name="description" content="">
  <meta name="author" content="">

  <title>The ERP Blog</title>

  <!-- Bootstrap core CSS -->
  <!--<link href="../../../../../../../../Users/Saul%20Feliciano/Desktop/blogbootstrap/startbootstrap-blog-post-gh-pages/vendor/bootstrap/css/bootstrap.min.css" rel="stylesheet">-->

  <!-- Custom styles for this template -->
  <!--<link href="../../../../../../../../Users/Saul%20Feliciano/Desktop/blogbootstrap/startbootstrap-blog-post-gh-pages/css/blog-post.css" rel="stylesheet">-->

  <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous">
  <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js" integrity="sha384-JjSmVgyd0p3pXB1rRibZUAYoIIy6OrQ6VrjIEaFf/nJGzIxFDsf4x0xIM+B07jRM" crossorigin="anonymous"></script>
  <script src="https://code.jquery.com/jquery-3.3.1.slim.min.js" integrity="sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo" crossorigin="anonymous"></script>
  <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js" integrity="sha384-UO2eT0CpHqdSJQ6hJty5KVphtPhzWj9WO1clHTMGa3JDZwrnQq4sF86dIHNDz0W1" crossorigin="anonymous"></script>

</head>

<body>

  <!-- Navigation -->
  <nav class="navbar navbar-expand-lg navbar-dark bg-dark fixed-top">
    <div class="container">
      <a class="navbar-brand" href="#">ERP Post</a>
      <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarResponsive" aria-controls="navbarResponsive" aria-expanded="false" aria-label="Toggle navigation">
        <span class="navbar-toggler-icon"></span>
      </button>
      <div class="collapse navbar-collapse" id="navbarResponsive">
        <ul class="navbar-nav ml-auto">
          <li class="nav-item active">
            <a class="nav-link" href="/menu/1">Home
              <span class="sr-only">(current)</span>
            </a>
          </li>
          <li class="nav-item">
            <a class="nav-link" href="#">About</a>
          </li>
          <#if loggedUser?exists>
            <li class="nav-item">
              <a class="nav-link" href="#">${loggedUser.username}</a>
            </li>
          </#if>
          <#if loggedUser?exists>
            <li class="nav-item">
              <a class="nav-link" href="/disconnect">Desconectar</a>
            </li>
          <#else>
            <li class="nav-item">
              <a class="nav-link" href="/login">Login</a>
            </li>
          </#if>
        </ul>
      </div>
    </div>
  </nav>

  <!-- Page Content -->
  <div class="container">
    <br><br>
    <div class="row">

      <!-- Post Content Column -->
      <div class="col-lg-8">

        <!-- Title -->
        <h1 class="mt-4">${articulo.titulo}</h1>

        <!-- Author -->
        <p class="lead">
          by
          <a href="#">${articulo.autor.username}</a>
        </p>

        <hr>

        <!-- Date/Time -->
        <p>Posted on January 1, 2019 at 12:00 PM</p>

        <hr>

        <!-- Preview Image -->
        <img class="img-fluid rounded" src="http://placehold.it/900x300" alt="">

        <hr>

        <!-- Post Content -->
        <p>${articulo.cuerpo}</p>
        <hr>

        <!-- Post Tags -->
        <#if articulo.listaEtiquetas?size != 0>
          <h5>Tags:</h5>
          <#list articulo.listaEtiquetas as etq>
            <p id="postedTags">${etq.etiqueta}</p>
          </#list>
        </#if>
        <hr>

        <!-- edit comment-->
        <#if loggedUser?exists && (articulo.autor.username == loggedUser.username || loggedUser.administrador)>
          <a id="editButton" class="btn btn-primary">Edit</a>
          <br><br>
          <a id="deleteButton" class="btn btn-primary">Delete</a>
          <br><br>
        </#if>

        <#if loggedUser?exists && articulo.autor.username == loggedUser.username>
          <div id="mydiv" style="visibility:hidden">
            <form method="post" action="/updatePost/${articulo.id}">
              <div class="form-group">
                <input class="form-control" id="postTitle" name="postTitle" placeholder="Title" type="text">
                <textarea id="postContent"  name="postContent" class="form-control" rows="6"></textarea>
                <input id="tags" class="form-control" name="tags" placeholder="Tags" type="text">
              </div>
              <button id="postPost" type="submit" class="btn btn-primary">Submit</button>
            </form>
            <br><br>
          </div>
        </#if>

        <!-- Like and Dislike Buttons -->
        <#if loggedUser?exists>
          <div id="demo">
            <a id="likebutton" class="btn btn-primary">Like
              <span class="likes">${likes}</span>
            </a>
            <button id="dislikebutton" class="btn btn-primary">Dislike
              <span class="dislikes">${dislikes}</span>
            </button>
          </div>
        </#if>

        <!-- Comments Form -->
        <#if loggedUser?exists>
          <div class="card my-4">
            <h5 class="card-header">Leave a Comment:</h5>
            <div class="card-body">
              <form action="/saveComment/${articulo.id}">
                <div class="form-group">
                  <textarea name="commentContent" class="form-control" rows="3"></textarea>
                </div>
                <button id="upload-comment" type="submit" class="btn btn-primary">Submit</button>
              </form>
            </div>
          </div>
        </#if>



        <!-- Single Comment -->
        <div id="comment-container" class="media mb-4">
          <#list listaComentarios as comentario>
            <img class="d-flex mr-3 rounded-circle" src="http://placehold.it/50x50" alt="">
            <div class="media-body">
              <h5 class="mt-0">${comentario.autor.username}</h5>
              ${comentario.comentario}
            </div>
            <#if loggedUser?exists && (loggedUser == articulo.autor)>
              <form action="/deleteComment/${articulo.id}/${comentario.id}" method="post">
                <button id="delete-comment" type="submit" class="btn btn-primary">Delete</button>
              </form>
            </#if>
          </#list>
        </div>

      </div>

      <!-- Sidebar Widgets Column -->
      <div class="col-md-4">

        <!-- Search Widget -->
        <div class="card my-4">
          <h5 class="card-header">Search</h5>
          <div class="card-body">
            <div class="input-group">
              <input type="text" class="form-control" placeholder="Search for...">
              <span class="input-group-btn">
                <button class="btn btn-secondary" type="button">Go!</button>
              </span>
            </div>
          </div>
        </div>

        <!-- Categories Widget -->
        <div class="card my-4">
          <h5 class="card-header">Categories</h5>
          <div class="card-body">
            <div class="row">
              <div class="col-lg-6">
                <ul class="list-unstyled mb-0">
                  <li>
                    <a href="#">Web Design</a>
                  </li>
                  <li>
                    <a href="#">HTML</a>
                  </li>
                  <li>
                    <a href="#">Freebies</a>
                  </li>
                </ul>
              </div>
              <div class="col-lg-6">
                <ul class="list-unstyled mb-0">
                  <li>
                    <a href="#">JavaScript</a>
                  </li>
                  <li>
                    <a href="#">CSS</a>
                  </li>
                  <li>
                    <a href="#">Tutorials</a>
                  </li>
                </ul>
              </div>
            </div>
          </div>
        </div>

        <!-- Side Widget -->
        <div class="card my-4">
          <h5 class="card-header">Side Widget</h5>
          <div class="card-body">
            You can put anything you want inside of these side widgets. They are easy to use, and feature the new Bootstrap 4 card containers!
          </div>
        </div>

      </div>

    </div>
    <!-- /.row -->

  </div>
  <!-- /.container -->

  <!-- Footer -->
  <footer class="py-5 bg-dark">
    <div class="container">
      <p class="m-0 text-center text-white">Copyright &copy; Your Website 2019</p>
    </div>
    <!-- /.container -->
  </footer>

  <!-- Bootstrap core JavaScript -->
  <script src="resources/publico/startbootstrap-blog-post-gh-pages/vendor/jquery/jquery.min.js"></script>
  <script src="resources/publico/startbootstrap-blog-post-gh-pages/vendor/bootstrap/js/bootstrap.bundle.min.js"></script>
  <script src="https://code.jquery.com/jquery-3.4.1.js"
          integrity="sha256-WpOohJOqMqqyKL9FccASB9O0KwACQJpFTUBLTYOVvVU="
          crossorigin="anonymous"></script>
  <script type="text/javascript">
    $(document).ready(function(){
      $('#uploadComment').on('click', function(){
        var comentario = $(textarea.form-control).val();
        var usuario = "Tu crazy wawawa";
        var html = '<img class="d-flex mr-3 rounded-circle" src="http://placehold.it/50x50" alt="">\
                <div class="media-body">\
                <h5 class="mt-0">' + usuario + '</h5>' + comentario + '</div>';
        console.log(html);
        $('#comment-container').append(html);
      });

      $('#editButton').click(function () {
        var mydiv = document.getElementById("mydiv");
        mydiv.style.visibility="visible";
        var title = "${articulo.titulo}";
        $('#postTitle').val(
                title
        );
        var body = "${articulo.cuerpo}";
        $('#postContent').val(
                body
        );
        var tags;
        var etiquetas = $("#postedTags");
        var i;
        <#--for(i = 0; i < ${articulo.listaEtiquetas?size}; i++)-->
        <#--{-->
        <#--    console.log(etiquetas[i]);-->
        <#--    tags+= etiquetas[i];-->
        <#--    tags+= " ";-->
        <#--}-->
        <#--etiquetas.forEach(function(etq) {-->
        <#--    tags.concat(etq.etiqueta);-->
        <#--    tags.concat(" ");-->
        <#--});-->
        <#--$('#tags').val(-->
        <#--   tags-->
        <#--);-->
      });

      $("#deleteButton").click(function () {
        var ruta = "/deletePost/${articulo.id}";
        console.log(ruta);
        document.location.href = ruta.toString();
      });

      <#if loggedUser?exists>
        var articleid = ${articulo.id};
       var username = "${loggedUser.username}";
      </#if>

      $('#likebutton').on('click', function(){
        var ruta = "/addLike/" + articleid + "/" + username;
        console.log(ruta);
        document.location.href = ruta.toString();
      });

      $('#dislikebutton').on('click', function(){
         var ruta = "/addDislike/" + articleid + "/" + username;
         console.log(ruta);
         document.location.href = ruta.toString();
       });
    });
  </script>
</body>

</html>
