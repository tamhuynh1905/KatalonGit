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
import com.kms.katalon.core.testdata.CSVData

import internal.GlobalVariable

import org.openqa.selenium.WebElement
import org.supercsv.cellprocessor.ParseInt
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

class EmployeePage {
	/**
	 * verifyFilterResultSuccessful
	 */
	@Keyword
	static boolean verifyFilterResultSuccessful(TestObject toResult, String expectedResult) {
		boolean isEqual = false
		String strResult = ''
		String strReplace = ''
		String[] lstResult
		if(MainPage.elementPresent(toResult)) {
			strResult = WebUiBuiltInKeywords.getText(toResult)
			lstResult = strResult.split(' ')
			String strResultNumber = ''

			if(lstResult[0].contains('(')) {
				strResultNumber = lstResult[0].toString().replace('(', '').replace(')', '')
				if(strResultNumber.toInteger() > 1) {
					strReplace = strResult.replace(strResultNumber, '*')
				}else {
					isEqual = false
				}
			}else {
				strReplace = strResult
			}

			if (strReplace == expectedResult) {
				isEqual = true
			}
		}
		return isEqual
	}


	/**
	 * accessSubPageMenuItem
	 */
	@Keyword
	static accessSubPageMenuItem(String subPageMenuItemName, String subPageSubMenuItemName) {
		WebUiBuiltInKeywords.click(findTestObject('Main/mnuHorizonConfigIcon'))
		WebUiBuiltInKeywords.click(findTestObject('Main/mnuHorizonConfigDropdownItem', [('dropdownMenuItem') : subPageSubMenuItemName]))
	}

	/**
	 * accessSubPageMenuItemSuccessful
	 */
	@Keyword
	static accessSubPageMenuItemSuccessful(String subPageMenuItemName, String subPageSubMenuItemName) {
		accessSubPageMenuItem(subPageMenuItemName, subPageSubMenuItemName)
		WebUiBuiltInKeywords.verifyElementPresent(findTestObject('objWebPIM/Main/lbl_SubPageMenuItemHeader', [('headerName') : subPageSubMenuItemName]), GlobalVariable.G_Timeout)
	}

	/**
	 * insertToCSVFile
	 */
	@Keyword
	static String[] insertToCSVFile(File csvFile) {
		Date todaysDate = new Date();
		String format = 'yyyy-MM-dd'
		def formattedDate = todaysDate.format(format)
		String firstName = 'Tam ' + formattedDate + todaysDate.getTime()
		String middleName = 'Minh'
		String lastName = 'Huynh'

		String[] fullName = [firstName, middleName, lastName]

		String record = "${firstName}, ${middleName}, ${lastName},,,,,,,,,,,,,,,,,,,"
		csvFile.append("\n")
		csvFile.append(record)

		return fullName
	}

	/**
	 * insertToCSVFile
	 */
	@Keyword
	static boolean browseFile(String filePath) {
		WebElement we2 = WebUiBuiltInKeywords.findWebElement(findTestObject('Employee/input_File'))
		we2.sendKeys(filePath)

		WebUiBuiltInKeywords.verifyElementText(findTestObject('Employee/lblSelectedBrowseFile'), GlobalVariable.G_FileName)
	}

	/**
	 * verifyNewEmployee
	 */
	@Keyword
	static String verifyNewEmployee (String[] strFullName) {
		List<WebElement> lst = WebUiBuiltInKeywords.findWebElements(findTestObject('Object Repository/Main/tblFilteredRows'), GlobalVariable.G_Timeout)
		String strFirstMiddle = "'" + strFullName[0] + "'  '" + strFullName[1] + "'"
		String strLast = strFullName[2]
		boolean isPresent = false
		String strFirstMiddleName = ''
		String strLastName = ''

		if(lst.size() == 1) {
			strFirstMiddleName = WebUiBuiltInKeywords.getText(findTestObject('Main/tblFilteredCell', [('rowNumber') : 1, ('cellNumber') : 3]))
			strLastName = WebUiBuiltInKeywords.getText(findTestObject('Main/tblFilteredCell', [('rowNumber') : 1, ('cellNumber') : 4]))
			if(strFirstMiddleName == strFirstMiddle) {
				strLastName.replaceAll("'", "")
				if(strLastName == strLast) {
					isPresent = true
				}
			}
		}
		return isPresent
	}

	/**
	 * verifyNewEmployee
	 */
	@Keyword
	static boolean deleteNewEmployee (String[] strFullName) {
		boolean isPresent = verifyNewEmployee(strFullName)
		boolean isDeleted = false
		if(isPresent) {
			WebUiBuiltInKeywords.click(findTestObject('Main/tblFilteredCellButton', [('rowNumber') : 1, ('cellNumber') : 9]))

			WebUiBuiltInKeywords.click(findTestObject('Object Repository/Main/btnDelete'))

			String strMessage = 'Successfully Deleted'
			WebUiBuiltInKeywords.verifyElementText(findTestObject('Leave/lblToastMessage'), strMessage)
			isDeleted = true
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
	def clickElement(TestObject to) {
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