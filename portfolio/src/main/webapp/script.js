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

async function getFormMessage() {
  const response = await fetch('/data');
  const jsonComments = await response.text();
  const comments = JSON.parse(jsonComments);
  
  Object.keys(comments).forEach(function(commentIndex){
    const node = document.createElement('p');
    const textnode = document.createTextNode(comments[commentIndex]);
    node.appendChild(textnode);
    document.getElementById('comments-container').appendChild(node); 
  });
}

const animateScrollTop = (targetClass) => {
  $('html').animate({
    scrollTop: $('.' + targetClass).offset().top
  }, 2000);
};

$(document).ready(function() {
  $('.contact-form').submit(function(event){
      event.preventDefault();
      getFormMessage();
  });

  $('a').click(function() {
    animateScrollTop($(this).attr('href').substr(1))
  });
  
});
