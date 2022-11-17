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
import org.openqa.selenium.Keys
import org.openqa.selenium.WebElement

import com.kms.MainPage
import com.kms.LeavePage

'TEST DATA'
def pageName = 'Leave'
def pageTitle = 'Leave List'
def isActionPage = true
def autoExpand = false
String leaveInfo = ''
def remainDaysBefore = 0
def pendingLeaveRow = 0

'TEST STEPS'
'Step 1 - Open browser and login in OrangeHRM page'
MainPage.loginPageSuccessful()

'Step 2 - Go to Leave page'
MainPage.accessPageSuccessful(pageName, pageTitle)

'Step 3 - Go to Apply'
pageName = 'Apply'
pageTitle = 'Apply Leave'
MainPage.accessSubPageSuccessful(pageName, pageTitle, isActionPage)

'Step 4 - Select value for Leave Type'
MainPage.selectFromDropdownSuccessful(type, typeValue, autoExpand)
leaveInfo += typeValue + ','

remainDaysBefore = MainPage.getElementText(findTestObject('Leave/lbl_Value', [('fieldName') : balance]))

'Step 5 - Select from date'
fromDate = MainPage.setDate(findTestObject('Leave/dtpFromDateField'), 0)

'Step 6 - Select to date'
toDate = MainPage.setDate(findTestObject('Leave/dtpToDateField'), addNextDays)
if(fromDate == toDate) {
	leaveInfo += toDate.toString() + ','
}else {
	leaveInfo += fromDate.toString() + ' to ' + toDate.toString() + ','
}

'Step 7 - Input comment'
MainPage.inputTextSuccessful(findTestObject('Main/txaFieldName', [('labelName') : comment]), commentValue)
leaveInfo += commentValue + ','

'Step 9 - Select value for Partial Day/Duration'
if(addNextDays != 0) {
	MainPage.selectFromDropdownSuccessful(partday, partdayValue, autoExpand)
	MainPage.selectFromDropdownSuccessful(duration, uncurrentDurationValue, autoExpand)
}
	
'Step 9 - Click Apply button'
MainPage.clickElement(findTestObject('Main/btnSubmit'))
	
'Step 10 - Verify apply message'
WebUI.verifyElementText(findTestObject('Leave/lblToastMessage'), result)

'Step 11 - Verify amount of Leave Balance'
MainPage.selectFromDropdownSuccessful(type, typeValue, autoExpand)

remainDaysAfter = MainPage.getElementText(findTestObject('Leave/lbl_Value', [('fieldName') : balance]))
leaveInfo += remainDaysAfter.substring(0, 5).toString() + ','

numberOfDayLeave = remainDaysBefore.substring(0, 5).toDouble() - remainDaysAfter.substring(0, 5).toDouble()
if(numberOfDayLeave > 0 ) {
	println("Amount of 'Leave Balance' is reduced by ${numberOfDayLeave}")
}else {
	println("Amount of 'Leave Balance' is not reduced by ${numberOfDayLeave}")
}
numberOfDayLeave = String.format("%.2f", numberOfDayLeave)
leaveInfo += numberOfDayLeave.toString() + ','
leaveInfo += "${status} (${numberOfDayLeave})" + ','
println(leaveInfo)

'Step 12 - Verify new created Pending Leave'
pageName = 'My Leave'
pageTitle = 'My Leave List'
isActionPage = false
MainPage.accessSubPageSuccessful(pageName, pageTitle, isActionPage)

fromDate = MainPage.setDate(findTestObject('Leave/dtpFromDateField'), 0)
toDate = MainPage.setDate(findTestObject('Leave/dtpToDateField'), addNextDays)

MainPage.selectFromDropdownSuccessful(type, typeValue, autoExpand)

MainPage.clickElement(findTestObject('Main/btnSubmit'))

'Step 13 - Cancel new created pending leave'
pendingLeaveRow = LeavePage.verifyMyLeave(leaveInfo.toString())
if(pendingLeaveRow > 0) {
	println("The leave status is ${status}")
	if(LeavePage.deletePendingLeave(pendingLeaveRow, resultUpdate)) {
		println("Cancel ${status} leave success")
	}else {
		println("Cannot cancel ${status} leave success")
	}
}else {
	println("The leave status is not ${status}")
}

'Step 14 - Close the browser'
WebUI.closeBrowser()


