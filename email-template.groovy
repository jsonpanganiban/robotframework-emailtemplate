
<!-- Robot Framework Results -->
<%
import java.text.DateFormat
import java.text.SimpleDateFormat
%>
<STYLE>
BODY, TABLE, TD, TH, P {
  font-family:Verdana,Helvetica,sans serif;
  font-size:11px;
  color:black;
}
h1 { color:black; }
h2 { color:black; }
h3 { color:black; }
TD.bg1 { color:white; background-color:#0000C0; font-size:120% }
TD.bg2 { color:white; background-color:#4040FF; font-size:110% }
TD.bg3 { color:white; background-color:#8080FF; }
TD.test_passed { color:blue; }
TD.test_failed { color:red; }
TD.console { font-family:Courier New; }
</STYLE>
<BODY>
<h4><b>Automation Test Results</b></h4>
<TABLE>
  <TR><TD>Project URL:</TD><TD><A href="${rooturl}${project.url}">${rooturl}${project.url}</A></TD></TR>
  <TR><TD>Log URL:</TD><TD><A href="${rooturl}${build.url}robot/report/log.html"> Open Log.html</A></TD></TR>
</TABLE>
</BODY>
<BR/>
<%
def robotResults = false
def actions = build.actions // List<hudson.model.Action>
actions.each() { action ->
    if( action.class.simpleName.equals("RobotBuildAction") ) { //

    hudson.plugins.robot.RobotBuildAction
    robotResults = true %>
    <table>
        <tr>
            <td style="color:black; font-size:150%;">
            <b><h4><u>Test Summary:</u></h4></b>
            </td>
        </tr>
    </table>
                <br/>
    <table id="robot-summary-table" style="border-collapse:collapse;text-align:center;">
                                <tr>
                                                <th style="font-weight:normal;padding:0 10px;">Type </th>
                                                <th style="font-weight:normal;padding:0 10px;">Total</th>
                                                <th style="font-weight:normal;padding:0 10px;">Failed</th>
                                                <th style="font-weight:normal;padding:0 10px;">Passed</th>
                                                <th style="font-weight:normal;padding:0 10px;">Pass %</th>
                                </tr>
                                <tr style="font-weight:bold">
                                                <th style="font-weight:normal">Critical tests</th>
                                                <td style="border-right:1px solid #000;border-bottom:1px solid #000;">${action.result.criticalTotal}</td>
                                                <td style="border-right:1px solid #000;border-bottom:1px solid #000;color:red">${action.result.criticalFailed}</td>
                                                <td style="border-right:1px solid #000;border-bottom:1px solid #000;color:green">${action.result.criticalPassed}</td>
                                                <td style="border-bottom:1px solid #000;">${action.criticalPassPercentage}</td>
                                </tr>
                                <tr style="font-weight:bold">
                                <th style="font-weight:normal">All tests</th>
                                                <td style="border-right:1px solid #000;">${action.result.overallTotal}</td>
                                                <td style="border-right:1px solid #000;color:red">${action.result.overallFailed}</td>
                                                <td style="border-right:1px solid #000;color:green">${action.result.overallPassed}</td>
                                                <td>${action.overallPassPercentage}</td>
                                </tr>
                                <tr style="font-weight:bold">
                                                <th style="font-weight:normal">Duration</hd>
                                                <td colspan=4 align="right">${action.result.humanReadableDuration}</td>
                                </tr>
                                <tr><td><br></td></tr>
                </table>
    <BR/>
        <table>
        <tr>
            <td style="color:black; font-size:150%;">
            <b><h4><u>Test Execution Results</u></h4></b>
            </td>
        </tr>
    </table>

    <table cellspacing="0" cellpadding="4" border="1" align="left">
        <thead>
            <tr bgcolor="#F3F3F3">
                <td><b>Test Name</b></td>
                <td><b>Status</b></td>
                <td><b>Error Details</b></td>
            </tr>

        </thead>
        <tbody>

        <% def suites1 = action.result.allSuites
        suites1.each() { suite ->
        def currSuite = suite
        def suiteName = currSuite.displayName
        // ignore top 2 elements in the structure as they are placeholders
        while (currSuite.parent != null && currSuite.parent.parent != null) {
        currSuite = currSuite.parent
        suiteName = currSuite.displayName + "." + suiteName
        } %>
            <tr>
                <td colspan="3">
                    <b><%= suiteName %></b>
                </td>
            </tr>
        <%  DateFormat format = new SimpleDateFormat("yyyyMMdd HH:mm:ss.SS")
        def execDateTcPairs = []
        suite.caseResults.each() { tc ->
        Date execDate = format.parse(tc.starttime)
        execDateTcPairs << [execDate, tc]
        }
        // primary sort execDate, secondary displayName
        def i = 1
        execDateTcPairs.each() {
        def execDate = it[0]
        def tc = it[1]  %>
            <tr>
                <td><a href="${rooturl}${build.url}robot/report/log.html#s1-s1-t<%= i%>"><%= tc.displayName %></a></td>
                <% i = i + 1 %>
                <td style="color: <%= tc.isPassed() ? "#66CC00" : "#FF3333" %>"><%= tc.isPassed() ? "PASS" : "FAIL" %></td>
                         <td><%      if ( tc.errorMsg == null ) {
                        tc.errorMsg=""
    } else {
         tc.errorMsg
}%>
<%= tc.errorMsg %>
</td>
            </tr>
        <%  } // tests
        } // suites %>
        </tbody>
    </table>
    <%
    } // robot results
    }
if (!robotResults) { %>
    <p>No Robot Framework test results found.</p>
    <%
    }
    %>
