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

package uk.gov.hmrc.perftests.insights.service

import io.gatling.http.Predef.HttpHeaderNames
import play.api.libs.ws.StandaloneWSResponse
import uk.gov.hmrc.perftests.insights.InsightsRequests.baseUrlFor
import uk.gov.hmrc.perftests.insights.client.HttpClientHelper
import uk.gov.hmrc.perftests.insights.util.Logging

trait WatchlistTestOnlyDataService extends HttpClientHelper with Logging {

  val baseUrl: String = baseUrlFor("phone-number-insights-proxy")

  def headers: Seq[(String, String)] =
    Seq(
      HttpHeaderNames.ContentType.toString -> "application/json",
      HttpHeaderNames.UserAgent.toString   -> "phone-number-insights-performance-tests",
      "X-Correlation-ID"                   -> "performance-test"
    )

  private def readPhoneNumbersFromFeederFile(): Seq[String] = {
    val source = scala.io.Source.fromResource("data/phone_numbers.csv")
    try
      source
        .getLines()
        .drop(1)
        .map(_.split(","))
        .collect { case Array(phone, risk, _) if risk.trim != "0" => phone.trim }
        .toSeq
    finally source.close()
  }

  def insertWatchlistPhoneNumbers(numberOfGeneratedPhoneNumbers: Int): Unit = {
    val feederPhoneNumbersOnWatchlist = readPhoneNumbersFromFeederFile()
      .map(pn => s""""$pn"""")
      .mkString(",")

    val request =
      s"""{
         |  "generatedEntries":{
         |    "numberOfEntries":$numberOfGeneratedPhoneNumbers
         |   },
         |  "manualEntries":{
         |    "phoneNumbers":[$feederPhoneNumbersOnWatchlist]
         |   }
         |}""".stripMargin

    val response: StandaloneWSResponse =
      post(s"$baseUrl/phone-number-insights-proxy/test-only/watchlist/data/create", request, headers: _*)

    logger.info(s"Inserted phone numbers to watchlist, response status: ${response.status} and body: ${response.body}")
  }

  def deleteWatchlistPhoneNumbers(): Unit = {
    val response: StandaloneWSResponse =
      delete(s"$baseUrl/phone-number-insights-proxy/test-only/watchlist/data/delete", headers: _*)

    logger.info(s"Deleted phone numbers from watchlist, response status: ${response.status} and body: ${response.body}")
  }
}
