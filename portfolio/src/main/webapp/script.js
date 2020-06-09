// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the 'License');
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an 'AS IS' BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
$(document).ready(() => {
  getComments();

  $('#deleter').click((event) => {
    deleteComments(event);
  });

  $('a').click(() => {
    animateScrollTop($(this).attr('href').substr(1));
  });

  $('.contact-form').submit((event) => {
    event.preventDefault();
    const name = $('.form-name').val();
    const comment = $('.form-comment').val();
    postComment(comment, name);
  });

  $('#comments-editor').submit((event) => {
    event.preventDefault();
    $.ajax({
      url: '/comments',
      type: 'GET',
      data: `&num=${$('.comments-num').val()}`,
      success: (response) => {
        const jsonComments = response;
        const comments = jsonComments.comments;
        $('.comments-section').empty();
        for (const comment of comments) {
          addCommentToDOM('.comments-section', comment);
        }
      }
    });
  });

  $('#image-upload-form').on('load', fetchBlobstoreUrlAndShowForm());
  $('#image-upload-form').submit((event) => {
    event.preventDefault();
    const action = $("#image-upload-form").attr('action');
    $.ajax({
      type: 'POST',
      url: action,
      cache: false,
      data : new FormData($("#image-upload-form")[0]),
      processData : false,
      contentType : false,
      success: (data) => {
        addImageToImagesContainer(data);
      }
    });
  });
});

function addImageToImagesContainer(data) {
  $('#images-container').append(`<img src=${data.imageUrl}><img>`);
}

function deleteComments(event) {
  event.preventDefault();
  $.ajax({
    url: '/delete-data',
    type: 'POST',
    success: (response) => {
      $('.comments-section').empty();
    }
  });
}

function fetchBlobstoreUrlAndShowForm() {
  fetch('/blobstore-upload-url')
    .then((response) => {
      return response.text();
    })
    .then((imageUploadUrl) => {
      const messageForm = document.getElementById('image-upload-form');
      messageForm.action = imageUploadUrl;
      messageForm.classList.remove('hidden');
    });
}

const animateScrollTop = (targetClass) => {
  $('html').animate({
      scrollTop: $('.' + targetClass).offset().top,
    },
    2000
  );
};

function addCommentToDOM(selector, comment) {
  $(selector).append(`<p>${comment.comment}</p>`);
  $(selector).append(`<p>${comment.name}</p>`);
}

async function getComments() {
  const response = await fetch('/data', {
    method: 'GET',
  });

  const jsonResponse = await response.text();
  const jsonComments = JSON.parse(jsonResponse);
  const comments = jsonComments.comments;
  for (const comment of comments) {
    addCommentToDOM('.comments-section', comment);
  }
}

async function postComment(comment, name) {
  const data = {
    name,
    comment,
  };
  const response = await fetch('/data', {
    method: 'POST',
    body: JSON.stringify(data),
  });
  $('.comments-section').append(`<p>${comment}</p>`);
  $('.comments-section').append(`<p>${name}</p>`);
}
