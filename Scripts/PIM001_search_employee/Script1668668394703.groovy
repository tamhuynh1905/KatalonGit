import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import static com.kms.katalon.core.testobject.ObjectRepository.findWindowsObject
import com.kms.katalon.core.checkpoint.Checkpoint as Checkpoint
import com.kms.katalon.core.cucumber.keyword.CucumberBuiltinKeywords as CucumberKW
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords as Mobile
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.testcase.TestCase as TestCase
import com.kms.katalon.core.testdata.TestData as TestData
import com.kms.katalon.core.testng.keyword.TestNGBuiltinKeywords as TestNGKW
import com.kms.katalon.core.testobject.TestObject as TestObject
import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WS
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.windows.keyword.WindowsBuiltinKeywords as Windows
import internal.GlobalVariable as GlobalVariable
import org.openqa.selenium.Keys as Keys
import com.kms.MainPage
import com.kms.EmployeePage

'TEST DATA'
def pageName = 'PIM'
def pageTitle = 'Employee Information'
def autoExpand = false
def columnName = 'Id'
def sortType = 'Ascending'
def isFiltered = false
def columnNumber = 2

'TEST STEPS'
'Step 1 - Open browser and login in OrangeHRM page'
MainPage.loginPageSuccessful()

'Step 2 - Go to PIM page'
MainPage.accessPageSuccessful(pageName, pageTitle)

'Step 3 - Expand filter secion (if it is not expand)'
MainPage.expandFilterSection(findTestObject('Main/lblFieldName', [('labelName') : status]))

'Step 4 - Select Employee Status'
MainPage.selectFromDropdownSuccessful(status, statusValue, autoExpand)

'Step 5 - Select Include'
MainPage.selectFromDropdownSuccessful(include, includeValue, autoExpand)

'Step 6 - Click Search button'
MainPage.clickElement(findTestObject('Main/btnSubmit'))

'Step 7 - Verify search result'
isFiltered = EmployeePage.verifyFilterResultSuccessful(findTestObject('Employee/lblFilterResult'), result)

'Step 8 - Sort ID in Ascending, Sort Job Title in Descending'
if(isFiltered) {
	println("The result of the table is displayed as '${result}' and is more than 1")
	
	if(MainPage.sortTableColumnASC(columnName, sortType, columnNumber)) {
		println("The list of ${columnName} is sorted in ${sortType} order")
	}else {
		println("The list of ${columnName} is not sorted in ${sortType} order")
	}
	
	columnName = 'Job Title'
	sortType = 'Decending'
	columnNumber = 5
	if(MainPage.sortTableColumnDesc(columnName, sortType, columnNumber)) {
		println("The list of ${columnName} is sorted in ${sortType} order")
	}else {
		println("The list of ${columnName} is not sorted in ${sortType} order")
	}
}else {
	println("The result of the table is not displayed as '${result}' or is lesse than 1")
}

'Step 9 - Close the browser'
WebUI.closeBrowser()


