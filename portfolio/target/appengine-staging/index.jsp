<%@ page import="com.google.appengine.api.blobstore.BlobstoreService" %>
<%@ page import="com.google.appengine.api.blobstore.BlobstoreServiceFactory" %>
<% BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
   String uploadUrl = blobstoreService.createUploadUrl("/my-form-handler"); %>

<!DOCTYPE html>
<html lang="en" dir="ltr">

<head>
  <meta charset="utf-8">
  <link rel="stylesheet" href="style.css">
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
  <script type="text/javascript" src="script.js"></script>
  <title>Raymond's Portfolio</title>
</head>

<body>
  <div class="header-container">
    <a class="header-item" href="#home">Raymond C. Alvarez Velez</a>
    <a class="header-item" href="#about-container">About</a>
    <a class="header-item" href="#work-container">My Work</a>
    <a class="header-item" href="#projects-container">Projects</a>
    <a class="header-item" href="#footer-container">Contact</a>
  </div>
  <div class="content-container">
    <div class="about-container">
      <div class="image">
        <img src="images/stanley-11.jpg" alt="">
      </div>
      <div class="description">
        <p>Hi! My name is Raymond</p>
        <p>I'm an intern at Google within the Travel Packages Team based in Cambridge, Massachusetts</p>
        <p>My favorite things to do are exploring nature, target practice, and programming</p>
        <p>In this portfolio you will find the work I have done!</p>
        <p>The main CSS concepts I use here are flexbox and media queries</p>
      </div>
    </div>
    <div class="work-container">
      <div class="jobs-container">
        <div class="work">
          <div class="image">
            <img src="images/google-logo.png" alt="">
          </div>
          <div class="description">
            <p>google work experience</p>
          </div>
        </div>
        <div class="work">
          <div class="image">
            <img src="images/pitchdeck.png" alt="">
          </div>
          <div class="description">
            <p>pitchdeck work experience</p>
          </div>
        </div>
        <div class="work">
          <div class="image">
            <img src="images/devbootcamp.png" alt="">
          </div>
          <div class="description">
            <p>devbootcamp work experience</p>
          </div>
        </div>
        <div class="work">
          <div class="image">
            <img src="images/utsa.png" alt="">
          </div>
          <div class="description">
            <p>utsa work experience</p>
          </div>
        </div>
      </div>
      <div class="projects-container">
        <div class="description">
          <p>Auto Apply</p>
          <p>This is a project built with the purpose of applying to a bunch of jobs really fast</p>
        </div>
        <div class="description">
          <p>Hog Drone</p>
          <p>This project was built with the purpose of reducing the $400 million dollars in annual damages hogs cause in Texas</p>
        </div>
        <div class="description">
          <p>Funny Driving Detection</p>
          <p>This project was built with the purpose of catching funny people doing funny stuff while driving</p>
        </div>
      </div>
    </div>
  </div>
  <div class="footer-container">
    <form action="/data" class="contact-form" method="post">
      <p>Say Hi!</p>
      <input class="form-name" type="text" name="name" placeholder="Name">
      <input class="form-comment" type="text" name="comment" placeholder="Enter Comment">
      <button class="form-button" type="submit" name="button">Send</button>
    </form>
    <form  class="contact-form"  method="POST">
      <p>Type some text:</p>
      <textarea name="message"></textarea>
      <br/>
      <p>Upload an image:</p>
      <input type="file" name="image">
      <br/><br/>
      <button>Submit</button>
    </form>
    <div id="comments-container">
      <h1 class="comments-title">Comments</h1>
    </div>
  </div>
</body>

</html>
