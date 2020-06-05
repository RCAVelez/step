// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
$(document).ready(function() {
  getComments();

  $("a").click(function() {
    animateScrollTop($(this).attr("href").substr(1));
  });

  $(".contact-form").submit(function(event) {
    event.preventDefault();
    const name = $(".form-name").val();
    const comment = $(".form-comment").val();
    postComment(comment, name);
  });

  $(".comments-editor").submit(function(event) {
    event.preventDefault();
    $.ajax({
      url: '/comments',
      type: 'GET',
      data: "&num=" + $('.comments-num').val(),
      success: function(response) {
        const jsonComments = JSON.parse(response);
        const comments = jsonComments["comments"];
        $(".comments-section").empty();
        for (let index = 0; index < comments.length; index++) {
          const comment = comments[index];
          $('.comments-section').append(`<p>${comment.comment}</p>`);
          $('.comments-section').append(`<p>${comment.name}</p>`);
        }
      }
    });
  });
});

const animateScrollTop = (targetClass) => {
  $("html").animate({
      scrollTop: $("." + targetClass).offset().top,
    },
    2000
  );
};

async function getComments() {
  const response = await fetch("/data", {
    method: "GET"
  });
  const jsonResponse = await response.text();
  const jsonComments = JSON.parse(jsonResponse);
  const comments = jsonComments["comments"];
  for (let index = 0; index < comments.length; index++) {
    const comment = comments[index];
    $('#comments-section').append(`<p>${comment.comment}</p>`);
    $('#comments-section').append(`<p>${comment.name}</p>`);
  }
}

async function postComment(comment, name) {
  const data = {
    name: name,
    comment: comment
  };
  const response = await fetch("/data", {
    method: "POST",
    body: JSON.stringify(data),
  });
  const jsonComment = await response.text();
  const commentObj = JSON.parse(jsonComment);
  $('#comments-section').append(`<p>${commentObj.comment}</p>`);
  $('#comments-section').append(`<p>${commentObj.name}</p>`);
}
