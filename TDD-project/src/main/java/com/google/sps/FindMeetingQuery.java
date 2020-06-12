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

public final class FindMeetingQuery {
  public Collection<TimeRange> query(Collection<Event> events, MeetingRequest request) {
    Collection<TimeRange> availableMeetingTimes = new ArrayList<TimeRange>();

    // optionsForNoAttendees
    if (request.getAttendees().size() == 0) {
      return Arrays.asList(TimeRange.WHOLE_DAY);
    }

    // noOptionsForTooLongOfARequest
    if (request.getDuration() > TimeRange.WHOLE_DAY.duration()) {
      return Arrays.asList();
    }

    // noConflicts
    if (events.size() == 0) {
      return Arrays.asList(TimeRange.WHOLE_DAY);
    }

    // eventSplitsRestriction
    if (events.size() == 1) { // provide start time and end time of event
      final Event event = (Event) events.toArray()[0];
      final int start = event.getWhen().start();
      final int end = event.getWhen().end();
      return Arrays.asList(
          TimeRange.fromStartEnd(TimeRange.START_OF_DAY, start, false),
          TimeRange.fromStartEnd(end, TimeRange.END_OF_DAY, true));
    }
    return availableMeetingTimes;
  }
}
