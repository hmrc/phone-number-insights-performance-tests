/*
 * Copyright 2023 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package uk.gov.hmrc.perftests.insights

import uk.gov.hmrc.performance.simulation.PerformanceTestRunner
import uk.gov.hmrc.perftests.insights.InsightsRequests.checkWatchListforPhoneNumberInsights
import uk.gov.hmrc.perftests.insights.service.WatchlistTestOnlyDataService

class InsightsSimulation extends PerformanceTestRunner with WatchlistTestOnlyDataService {

  before {
    // tidy up from any previous failed runs
    deleteWatchlistPhoneNumbers()

    // insert test data - 50,000 generated phone numbers + phone numbers from the CSV file that are marked as being on the watchlist
    // 50,000 takes on average around 16 seconds to insert, refactoring may be required if we need to push this higher
    insertWatchlistPhoneNumbers(50000)
  }

  after {
    // tidy up test data by removing all phone numbers from the watchlist that were inserted for this test
    deleteWatchlistPhoneNumbers()
  }

  setup("check-watch-list-gateway", "Check watch list via Gateway") withRequests checkWatchListforPhoneNumberInsights

  runSimulation()
}
