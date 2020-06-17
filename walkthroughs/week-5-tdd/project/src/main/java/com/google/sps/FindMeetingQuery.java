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

package com.google.sps;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;

public final class FindMeetingQuery {
  public Collection<TimeRange> query(Collection<Event> events, MeetingRequest request) {
    Collection<TimeRange> availableMeetingTimes = new ArrayList<TimeRange>();

    if (request.getAttendees().size() == 0) {
      return Arrays.asList(TimeRange.WHOLE_DAY);
    }

    if (request.getDuration() > TimeRange.WHOLE_DAY.duration()) {
      return Arrays.asList();
    }

    if (events.size() == 0) {
      return Arrays.asList(TimeRange.WHOLE_DAY);
    }

    int prevTime = TimeRange.START_OF_DAY;

    for (Event event : events) {
      if (!eventContainsAnyNameInRequest(event.getAttendees(), request.getAttendees())) {
        continue;
      }

      final int eventStart = event.getWhen().start();
      final int eventEnd = event.getWhen().end();
      if (prevTime > eventStart) {
        if (prevTime < eventEnd) {
          prevTime = eventEnd;
        }
        continue;
      }

      if (blockHasSufficientTime((int) request.getDuration(), eventStart, prevTime)) {
        availableMeetingTimes.add(TimeRange.fromStartEnd(prevTime, eventStart, false));
      }
      prevTime = eventEnd;
    }
    if (prevTime != TimeRange.END_OF_DAY + 1) {
      availableMeetingTimes.add(TimeRange.fromStartEnd(prevTime, TimeRange.END_OF_DAY, true));
    }
    return availableMeetingTimes;
  }

  private boolean blockHasSufficientTime(int duration, int endTime, int startTime) {
    if (endTime - startTime >= duration) {
      return true;
    }
    return false;
  }

  private boolean eventContainsAnyNameInRequest(
      Set<String> eventAttendees, Collection<String> requestAttendees) {
    if (!Collections.disjoint(eventAttendees, requestAttendees)) {
      return true;
    }
    return false;
  }
}
