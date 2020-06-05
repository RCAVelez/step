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
$(document).ready(function () {
  getComments();
  $("a").click(function () {
    animateScrollTop($(this).attr("href").substr(1));
  });

  $(".contact-form").submit(function (event) {
    event.preventDefault();
    const name = $(".form-name").val();
    const comment = $(".form-comment").val();
    postComment(comment, name);
  });
});

const animateScrollTop = (targetClass) => {
  $("html").animate(
    {
      scrollTop: $("." + targetClass).offset().top,
    },
    2000
  );
};

async function getComments() {
  const response = await fetch("/data", {
    method: "GET"
  });
  const jsonComments = await response.text();
  console.log(jsonComments);
}

async function postComment(comment, name) {
  const data = { name: name, comment: comment };
  const response = await fetch("/data", {
    method: "POST",
    body: JSON.stringify(data),
  });
  const jsonComment = await response.text();
  const commentJson = JSON.parse(jsonComment);

  const commentNode = createNode("p", commentJson["comment"]);
  document.getElementById("comments-container").appendChild(commentNode);
  const nameNode = createNode("p", commentJson["name"]);
  document.getElementById("comments-container").appendChild(nameNode);

}

function createNode(tag, text) {
  let node = document.createElement(tag);
  const textnode = document.createTextNode(text);
  node.appendChild(textnode);
  return node;
}
