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

$(document).ready(function(){

    
    $('a[href="#home"]').click(function() {
       $('html,body').animate({
           scrollTop: $(".home").offset().top},
          2000);
    });

    $('a[href="#about"]').click(function() {
       $('html,body').animate({
           scrollTop: $(".about").offset().top},
          2000);
    });

    $('a[href="#myWork"]').click(function() {
       $('html,body').animate({
           scrollTop: $(".myWork").offset().top},
          2000);
    });

    $('a[href="#contact"]').click(function() {
       $('html,body').animate({
           scrollTop: $(".contact").offset().top},
          2000);
    });
});
