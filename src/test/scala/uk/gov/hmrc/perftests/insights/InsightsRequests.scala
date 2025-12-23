/*
 * Copyright 2025 HM Revenue & Customs
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

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.http.request.builder.HttpRequestBuilder
import uk.gov.hmrc.performance.conf.ServicesConfiguration

object InsightsRequests extends ServicesConfiguration {

  val baseUrl: String = baseUrlFor("phone-number-gateway")

  val checkWatchListforPhoneNumberInsights: HttpRequestBuilder =
    http("Check if number is on watch list")
      .post(s"$baseUrl/check/insights")
      .header(HttpHeaderNames.ContentType, "application/json")
      .header(HttpHeaderNames.UserAgent, "phone-number-insights-performance-tests")
      .header("X-Correlation-ID", "performance-test")
      .body(StringBody("""|{
                          |  "phoneNumber" : "#{phoneNumber}"
                          |}
                          |""".stripMargin))
      .asJson
      .check(status.is(200))
}
