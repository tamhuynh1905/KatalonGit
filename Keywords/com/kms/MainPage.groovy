package com.kms
import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import java.text.DateFormat
import java.text.SimpleDateFormat

import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.checkpoint.Checkpoint
import com.kms.katalon.core.checkpoint.CheckpointFactory
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

import internal.GlobalVariable

import org.openqa.selenium.WebElement
import org.openqa.selenium.WebDriver
import org.openqa.selenium.By
import org.openqa.selenium.Keys as Keys

import com.kms.katalon.core.mobile.keyword.internal.MobileDriverFactory
import com.kms.katalon.core.webui.driver.DriverFactory

import com.kms.katalon.core.testobject.RequestObject
import com.kms.katalon.core.testobject.ResponseObject
import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.testobject.TestObjectProperty

import com.kms.katalon.core.mobile.helper.MobileElementCommonHelper
import com.kms.katalon.core.util.KeywordUtil

import com.kms.katalon.core.webui.exception.WebElementNotFoundException


class MainPage {
	/**
	 * Login page
	 */
	@Keyword
	static loginPage() {
		//Access page
		WebUiBuiltInKeywords.openBrowser('')
		WebUiBuiltInKeywords.maximizeWindow()
		WebUiBuiltInKeywords.navigateToUrl(GlobalVariable.G_SiteURL)

		//Log in PIM page
		WebUiBuiltInKeywords.setText(findTestObject('Main/txtLogin', [('loginInfo') : 'username']), GlobalVariable.G_Username)
		WebUiBuiltInKeywords.setText(findTestObject('Main/txtLogin', [('loginInfo') : 'password']), GlobalVariable.G_Password)
		WebUiBuiltInKeywords.click(findTestObject('Main/btnSubmit'))
	}

	/**
	 * Login page Successful
	 */
	@Keyword
	static loginPageSuccessful() {
		loginPage()
		WebUiBuiltInKeywords.verifyElementPresent(findTestObject('Main/ddlUserLogin'), GlobalVariable.G_Timeout)
	}

	/**
	 * Access specific page
	 */
	@Keyword
	static accessPage(String pageName) {
		//Access page
		WebUiBuiltInKeywords.click(findTestObject('Main/mnuVerticalItem', [('menuItemName') : pageName]))
	}

	/**
	 * Access page Successful
	 */
	@Keyword
	static accessPageSuccessful(String pageName, String pageTitle) {
		def activeMenuItemName = WebUiBuiltInKeywords.getText(findTestObject('Main/mnuVerticalActiveItem'))
		if(activeMenuItemName != pageName) {
			accessPage(pageName)
			WebUiBuiltInKeywords.verifyElementPresent(findTestObject('Main/lblPageHeader', [('headerName') : pageTitle]), GlobalVariable.G_Timeout)
		}
	}

	/**
	 * accessSubPage
	 */
	@Keyword
	static accessSubPage(String subPageName) {
		WebUiBuiltInKeywords.click(findTestObject('Object Repository/Main/mnuHorizonItem', [('menuItemName') : subPageName]))
	}

	/**
	 * accessSubPageSuccessful
	 */
	@Keyword
	static accessSubPageSuccessful(String subPageName, String pageTitle, boolean isActionPage) {
		accessSubPage(subPageName)
		if(isActionPage) {
			WebUiBuiltInKeywords.verifyElementPresent(findTestObject('Main/lblSubPageHeader', [('headerName') : pageTitle]), GlobalVariable.G_Timeout)
		}else {
			WebUiBuiltInKeywords.verifyElementPresent(findTestObject('Main/lblPageHeader', [('headerName') : pageTitle]), GlobalVariable.G_Timeout)
		}
	}

	/**
	 * elementPresent
	 */
	@Keyword
	static boolean elementPresent(TestObject to) {
		return WebUiBuiltInKeywords.verifyElementPresent(to, GlobalVariable.G_ShortTimeOut)
	}

	/**
	 * expandFilterSection
	 */
	@Keyword
	static expandFilterSection(TestObject to) {
		if(elementPresent(to) == false) {
			clickElement(findTestObject('Main/icoExpandFilterSection'))
		}
	}

	/**
	 * selectFromDropdown
	 */
	@Keyword
	static selectFromDropdown(String fieldName, String fieldValue, boolean autoExpand) {
		if(autoExpand == false) {
			clickElement(findTestObject('Main/icoExpandDropdown', [('labelName') : fieldName]))
		}
		clickElement(findTestObject('Main/ddlSpecificItem', [('labelName') : fieldName, ('optionName') : fieldValue]))
	}

	/**
	 * verifySelectedTextFromDropdown
	 */
	@Keyword
	static verifySelectedTextFromDropdown(boolean isDefaultValue, String lblName, String lblValue) {
		String strExpected = '-- Select --'
		if(isDefaultValue == false) {
			strExpected = lblValue
		}
		String seletedText = WebUiBuiltInKeywords.getText(findTestObject('Main/ddlSelectedItem', [('labelName') : lblName]))
		boolean isMatch = false
		if(seletedText == strExpected) {
			isMatch = true
		}
		return isMatch
	}

	/**
	 * selectFromDropdownSuccessful
	 */
	@Keyword
	static selectFromDropdownSuccessful(String fieldName, String fieldValue, boolean autoExpand) {
		selectFromDropdown(fieldName, fieldValue, autoExpand)
		return verifySelectedTextFromDropdown(true, fieldName, fieldValue)
	}

	/**
	 * sortColumnASC
	 */
	@Keyword
	static boolean sortTableColumnASC(String columnName, String sortType, int columnNumber) {
		clickElement(findTestObject('Employee/icoExpandSort', [('columnHeaderName') : columnName]))
		clickElement(findTestObject('Employee/ddlSortItem', [('columnHeaderName') : columnName, ('sortType') : sortType]))

		int totalRows = countListTotalNumber(findTestObject('Main/tblFilteredRows'))

		boolean isAsc = true
		String strFirstItemValue = ''
		String strNextItemValue = ''
		if (totalRows > 1) {
			for (int i = 1; i <= (totalRows - 1); i++) {
				strFirstItemValue = WebUiBuiltInKeywords.getText(findTestObject('Main/tblFilteredCell', [('rowNumber') : i, ('cellNumber') : columnNumber]))
				strNextItemValue = WebUiBuiltInKeywords.getText(findTestObject('Main/tblFilteredCell', [('rowNumber') : i + 1, ('cellNumber') : columnNumber]))

				if (strFirstItemValue.isNumber() && strNextItemValue.isNumber()) {
					if (strFirstItemValue > strNextItemValue) {
						isAsc = false
						break
					}
				}
			}
		}
		return isAsc
	}

	/**
	 * sortColumnASC
	 */
	@Keyword
	static boolean sortTableColumnDesc(String columnName, String sortType, int columnNumber) {
		clickElement(findTestObject('Employee/icoExpandSort', [('columnHeaderName') : columnName]))
		clickElement(findTestObject('Employee/ddlSortItem', [('columnHeaderName') : columnName, ('sortType') : sortType]))

		int totalRows = countListTotalNumber(findTestObject('Main/tblFilteredRows'))

		boolean isDesc = true
		String strFirstItemValue = ''
		String strNextItemValue = ''

		if (totalRows > 1) {
			for (int i = 1; i <= (totalRows - 1); i++) {
				strFirstItemValue = WebUiBuiltInKeywords.getText(findTestObject('Main/tblFilteredCell', [('rowNumber') : i, ('cellNumber') : columnNumber]))
				strNextItemValue = WebUiBuiltInKeywords.getText(findTestObject('Main/tblFilteredCell', [('rowNumber') : i + 1, ('cellNumber') : columnNumber]))
				if (strNextItemValue.compareTo(strFirstItemValue) > 0) {
					isDesc = false;
					break;
				}
			}
		}
		return isDesc
	}

	/**
	 * setDate
	 */
	@Keyword
	static String setDate(TestObject objDate, int addDays) {
		String strDate = ''
		Date todaysDate = new Date();
		String format = 'yyyy-MM-dd'
		def formattedDate = todaysDate.format(format)
		if(addDays == 0) { //current date
			inputText(objDate, formattedDate)
		}else {//current date + adddays
			formattedDate = (todaysDate+addDays).format(format)
			inputText(objDate, formattedDate)
		}
//		if(isCurrent) {
//			inputText(objDate, formattedDate)
//		}else {
//			formattedDate = (todaysDate+addDays).format(format)
//			inputText(objDate, formattedDate)
//		}
		return formattedDate.toString()
	}
	
	/**
	 * get
	 */
	@Keyword
	static String getDate(boolean isCurrent, int addDays) {
//		String strDate = ''
		Date todaysDate = new Date();
		String format = 'yyyy-MM-dd'
		def formattedDate = todaysDate.format(format)
		if(addDays > 0) {
			formattedDate = (todaysDate+addDays).format(format)
		}
//		if(isCurrent) {
//			inputText(objDate, formattedDate)
//		}else {
//			formattedDate = (todaysDate+addDays).format(format)
//			inputText(objDate, formattedDate)
//		}
//		strDate = formattedDate.toString()
//		return strDate
		return formattedDate.toString()
	}

	/**
	 * imputText
	 */
	@Keyword
	static inputText(TestObject obj, String strInput) {
		WebUiBuiltInKeywords.sendKeys(obj, Keys.chord(Keys.CONTROL, 'a'))
		WebUiBuiltInKeywords.sendKeys(obj, Keys.chord(Keys.DELETE))
		WebUiBuiltInKeywords.setText(obj, strInput)
	}

	/**
	 * imputText
	 */
	@Keyword
	static inputTextSuccessful(TestObject obj, String strInput) {
		inputText(obj, strInput)
		String strActualText = WebUiBuiltInKeywords.getText(obj)
		boolean isMatch = false
		if(strActualText == strInput) {
			isMatch = true
		}
		return isMatch
	}

	/**
	 * Set date to Date Element
	 */
	@Keyword
	static setDateSucessfully(TestObject objDate, boolean isCurrent) {
		setDate(objDate, isCurrent)
	}

	/**
	 * Return and format date
	 */
	@Keyword
	public static Date addDays(Date date, int days) {
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(date);
		cal.add(Calendar.DATE, days);
	}
	@Keyword
	static getDate(String formatDate, int addDays) {
		DateFormat dateFormat = new SimpleDateFormat(formatDate)
		Date dte
		addDays(dte, +addDays);
		return dateFormat.format(dte);
	}

	/**
	 * Refresh browser${}
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
			String strElementName = element.getText()
			KeywordUtil.logInfo("Clicking element " + strElementName)
			element.click()
			KeywordUtil.markPassed("Element " + strElementName + " has been clicked")
		} catch (WebElementNotFoundException e) {
			KeywordUtil.markFailed("Element not found")
		} catch (Exception e) {
			KeywordUtil.markFailed("Fail to click on element")
		}
	}

	/**
	 * Click element
	 * @param to Katalon test object
	 */
	@Keyword
	static String getElementText(TestObject to) {
		return WebUiBuiltInKeywords.getText(to)
	}

	/**
	 * countListTotalNumber
	 */
	@Keyword
	static int countListTotalNumber(TestObject objList) {
		List<WebElement> lst = WebUiBuiltInKeywords.findWebElements(objList, GlobalVariable.G_Timeout)
		return lst.size()
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