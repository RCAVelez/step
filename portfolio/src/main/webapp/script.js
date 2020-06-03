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
  const quote = await response.text();
  document.getElementById('quote-container').innerHTML = quote;
}

const animateScrollTop = (targetClass) => {
  $("html").animate({
    scrollTop: $("." + targetClass).offset().top
  }, 2000);
};

$(document).ready(function() {
  $(".form-button").click(function() {
    getFormMessage()
  });

  $('a').click(function() {
    animateScrollTop($(this).attr('href').substr(1))
  });
});
