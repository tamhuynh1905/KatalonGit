package com.kms
import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.checkpoint.Checkpoint
import com.kms.katalon.core.checkpoint.CheckpointFactory
import com.kms.katalon.core.cucumber.keyword.CucumberBuiltinKeywords as CucumberKW
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords
import com.kms.katalon.core.model.FailureHandling
import com.kms.katalon.core.testcase.TestCase
import com.kms.katalon.core.testcase.TestCaseFactory
import com.kms.katalon.core.testdata.TestData
import com.kms.katalon.core.testdata.TestDataFactory
import com.kms.katalon.core.testobject.ObjectRepository
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords
import com.kms.katalon.core.windows.keyword.builtin.ClickElementOffsetKeyword

import internal.GlobalVariable
import org.openqa.selenium.WebElement
import org.openqa.selenium.WebDriver
import org.openqa.selenium.By

import com.kms.katalon.core.mobile.keyword.internal.MobileDriverFactory
import com.kms.katalon.core.webui.driver.DriverFactory

import com.kms.katalon.core.testobject.RequestObject
import com.kms.katalon.core.testobject.ResponseObject
import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.testobject.TestObjectProperty

import com.kms.katalon.core.mobile.helper.MobileElementCommonHelper
import com.kms.katalon.core.util.KeywordUtil

import com.kms.katalon.core.webui.exception.WebElementNotFoundException

import java.lang.Integer


class LeavePage {

	/**
	 * verifyMyLeave
	 */
	@Keyword
	static int verifyMyLeave(String leaveInfo) {
		String[] lstLeaveInfo = leaveInfo.split(',')
		List<WebElement> lst = WebUiBuiltInKeywords.findWebElements(findTestObject('Main/tblFilteredRows'), GlobalVariable.G_Timeout)
		String strStatus = ''
		String strDate = ''
		String strLeaveType = ''
		String strComment = ''
		String strLeaveBalance = ''
		String strNumDay = ''

		int intRowNumber = 0

		for (int i = 1; i <= lst.size(); i++) {
			strStatus = WebUiBuiltInKeywords.getText(findTestObject('Main/tblFilteredCell', [('rowNumber') : i, ('cellNumber') : 7]))
			if(strStatus == lstLeaveInfo[5].toString()) {
				strDate = WebUiBuiltInKeywords.getText(findTestObject('Main/tblFilteredCell', [('rowNumber') : i, ('cellNumber') : 2]))
				if(strDate == lstLeaveInfo[1].toString()) {
					strLeaveType = WebUiBuiltInKeywords.getText(findTestObject('Main/tblFilteredCell', [('rowNumber') : i, ('cellNumber') : 4]))
					if(strLeaveType == lstLeaveInfo[0].toString()) {
						strComment = WebUiBuiltInKeywords.getText(findTestObject('Main/tblFilteredCell', [('rowNumber') : i, ('cellNumber') : 8]))
						if(strComment == lstLeaveInfo[2].toString()) {
							strLeaveBalance = WebUiBuiltInKeywords.getText(findTestObject('Main/tblFilteredCell', [('rowNumber') : i, ('cellNumber') : 5]))
							if(strLeaveBalance == lstLeaveInfo[3].toString()) {
								strNumDay = WebUiBuiltInKeywords.getText(findTestObject('Main/tblFilteredCell', [('rowNumber') : i, ('cellNumber') : 6]))
								if(strNumDay == lstLeaveInfo[4].toString()) {
									intRowNumber = i
								}
							}
						}
					}
				}
			}
		}
		return intRowNumber
	}
	
	/**
	 * verifyMyLeave
	 */
	@Keyword
	static boolean verifyAndDeleteMyExistedLeave(String expectedStatus, String expectedDate, int nextDays, String resultUpdate) {
//		String[] lstLeaveInfo = leaveInfo.split(',')
		List<WebElement> lst = WebUiBuiltInKeywords.findWebElements(findTestObject('Main/tblFilteredRows'), GlobalVariable.G_Timeout)
		String strStatus = ''
		String[] lstStatus
		String strDate = ''
		String[] lstExistedDate
		String[] lstExpectedDate
		boolean isVerify = false

		int intRowNumber = 0

		for (int i = 1; i <= lst.size(); i++) {
			strStatus = WebUiBuiltInKeywords.getText(findTestObject('Main/tblFilteredCell', [('rowNumber') : i, ('cellNumber') : 7]))
			lstStatus = strStatus.split(' ')
			
			if(lstStatus[0].toString() + ' ' + lstStatus[1].toString() == expectedStatus) {//only compare pending leave status not including number of day leave
				strDate = WebUiBuiltInKeywords.getText(findTestObject('Main/tblFilteredCell', [('rowNumber') : i, ('cellNumber') : 2]))
				
				
				
				
				if(strDate.contains('to')) {//existed pending leave is from..to
					lstExistedDate = strDate.split(' ')
					
					if(nextDays == 0) {//expected date is current date
						//to is past date
						
						//to is current date
						//to is future date
					}else {//expected date is from ..to
					
					}
				}else {//pending leave is specific date
					if(nextDays == 0) {//expected date is current date
						if(strDate == expectedDate) {
							//Delete existed pending leave
							if(deletePendingLeave(i, resultUpdate)) {
								isVerify = true
							}
						}else {
							isVerify = true
						}
					}else {//expected date is from ..to
						
					}
				}
				
			}
		}
		
		return isVerify
	}
	
	/**
	 * compareDate
	 */
	@Keyword
	static boolean compareDate(String strDate1, String strDate2) {
		
	}
	

	/**
	 * Refresh browser
	 */
	@Keyword
	static boolean deletePendingLeave(int intRowNumber, String strMessage) {
		boolean isDeleted = false
		if(intRowNumber>0) {
			clickElement(findTestObject('Main/tblFilteredCell', [('rowNumber') : intRowNumber, ('cellNumber') : 9]))
			if(WebUiBuiltInKeywords.verifyElementText(findTestObject('Leave/lblToastMessage'), strMessage)) {
				isDeleted = true
			}
		}
		return isDeleted
	}


	/**
	 * Refresh browser
	 */
	@Keyword
	def refreshBrowser() {
		KeywordUtil.logInfo("Refreshing")
		WebDriver webDriver = DriverFactory.getWebDriver()
		webDriver.navigate().refresh()
		KeywordUtil.markPassed("Refresh successfully")
	}

	/**
	 * Click element
	 * @param to Katalon test object
	 */
	@Keyword
	static clickElement(TestObject to) {
		try {
			WebElement element = WebUiBuiltInKeywords.findWebElement(to);
			KeywordUtil.logInfo("Clicking element")
			element.click()
			KeywordUtil.markPassed("Element has been clicked")
		} catch (WebElementNotFoundException e) {
			KeywordUtil.markFailed("Element not found")
		} catch (Exception e) {
			KeywordUtil.markFailed("Fail to click on element")
		}
	}

	/**
	 * Get all rows of HTML table
	 * @param table Katalon test object represent for HTML table
	 * @param outerTagName outer tag name of TR tag, usually is TBODY
	 * @return All rows inside HTML table
	 */
	@Keyword
	def List<WebElement> getHtmlTableRows(TestObject table, String outerTagName) {
		WebElement mailList = WebUiBuiltInKeywords.findWebElement(table)
		List<WebElement> selectedRows = mailList.findElements(By.xpath("./" + outerTagName + "/tr"))
		return selectedRows
	}
}