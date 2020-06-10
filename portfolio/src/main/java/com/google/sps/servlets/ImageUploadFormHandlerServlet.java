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

package com.google.sps.servlets;

import com.google.appengine.api.blobstore.BlobInfo;
import com.google.appengine.api.blobstore.BlobInfoFactory;
import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.api.images.ImagesService;
import com.google.appengine.api.images.ImagesServiceFactory;
import com.google.appengine.api.images.ServingUrlOptions;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** Grab image uploaded to blobstore based on url, and present it */
@WebServlet("/image-upload-form-handler")
public class ImageUploadFormHandlerServlet extends HttpServlet {
  private final BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
  private final ImagesService imagesService = ImagesServiceFactory.getImagesService();

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String imageUrl = getUploadedFileUrl(request, "image");

    JsonObject imageObject = new JsonObject();
    imageObject.addProperty("imageUrl", imageUrl);
    String json = imageObject.toString();

    response.setContentType("application/json");
    response.getWriter().println(json);
  }

  /** returns url of uploaded file, returns "" if not found */
  private String getUploadedFileUrl(HttpServletRequest request, String formInputElementName)
      throws IOException {
    Map<String, List<BlobKey>> blobs = blobstoreService.getUploads(request);

    if (blobs.containsKey("image") == false) {
      throw new IOException("Please upload an image");
    }
    List<BlobKey> blobKeys = blobs.get("image");

    if (blobKeys == null || blobKeys.isEmpty()) {
      throw new IOException("Please upload an image");
    }

    BlobKey blobKey = blobKeys.get(0);

    BlobInfo blobInfo = new BlobInfoFactory().loadBlobInfo(blobKey);
    if (blobInfo.getSize() == 0) {
      blobstoreService.delete(blobKey);
      throw new IOException("Loaded blob size is 0");
    }

    ServingUrlOptions options = ServingUrlOptions.Builder.withBlobKey(blobKey);

    return imagesService.getServingUrl(options);
  }
}
