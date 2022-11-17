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
def searchText = 'Employment Status'
def pageName = 'PIM'
def pageTitle = 'Employee Information'
def autoExpand = false
def expectedResult = 'No Records Found'

'TEST STEPS'
'Step 1 - Open browser and login in OrangeHRM page'
MainPage.loginPageSuccessful()

'Step 2 - Go to PIM page'
MainPage.accessPageSuccessful(pageName, pageTitle)

'Step 3 - Expand filter secion (if it is not expand)'
MainPage.expandFilterSection(findTestObject('Main/lblFieldName', [('labelName') : name]))

'Step 4 - Input Employee Name'
WebUI.setText(findTestObject('Main/txtFieldName', [('labelName') : name]), nameValue)

'Step 5 - Input Employee Id'
WebUI.setText(findTestObject('Main/txtFieldName', [('labelName') : id]), idValue)

'Step 6 - Select Employee Status'
MainPage.selectFromDropdownSuccessful(status, statusValue, autoExpand)

'Step 7 - Select Employee Include'
MainPage.selectFromDropdownSuccessful(include, includeValue, autoExpand)

'Step 8 - Select Job Title'
MainPage.selectFromDropdownSuccessful(job, jobValue, autoExpand)

'Step 9 - Select Sub Unit'
MainPage.selectFromDropdownSuccessful(unit, unitValue, autoExpand)

'Step 10 - Click Search button'
MainPage.clickElement(findTestObject('Main/btnSubmit'))

'Step 11 - Verify search result'
if(EmployeePage.verifyFilterResultSuccessful(findTestObject('Employee/lblFilterResult'), result)) {
	println("The result of the search table is displayed as '${result}'")
}else {
	println("The result of the search table is not displayed as '${result}'")
}

'Step 12 - Close the browser'
WebUI.closeBrowser()
