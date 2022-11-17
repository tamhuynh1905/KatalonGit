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

import javax.swing.JFileChooser
import java.io.File
import java.nio.file.Path
import java.nio.file.Paths

import com.kms.MainPage
import com.kms.LeavePage
import com.kms.EmployeePage

'TEST DATA'
def searchText = 'Employment Status'
def pageName = 'PIM'
def pageTitle = 'Employee Information'
def importMessage = '1 Record Successfully Imported'
def isActionPage = false
def fieldName = 'Employee Name'
def fieldValue = 'CAN - Bereavement'
def uploadMessage = ''

'TEST STEPS'
'Step 1 - Open browser and login in OrangeHRM page'
MainPage.loginPageSuccessful()

'Step 2 - Go to PIM page - Configuration/Data Import'
MainPage.accessPageSuccessful(pageName, pageTitle)

pageName = 'Configuration'
pageTitle = 'Data Import'
EmployeePage.accessSubPageMenuItem(pageName, pageTitle)

'Step 3 - Download file'
MainPage.clickElement(findTestObject('Employee/lnkDownload'))

'Step 4 - Insert data to download file'
WebUI.verifyElementPresent(findTestObject('Employee/lnkDownload'), GlobalVariable.G_Timeout)
Path downloadDir = Paths.get(System.getProperty("user.home"), "Downloads")
Path fileToUpload = downloadDir.resolve(GlobalVariable.G_FileName)
File csvFile = new File(fileToUpload.toString())
strFullName = EmployeePage.insertToCSVFile(csvFile)

'Step 5 - Upload file'
EmployeePage.browseFile(fileToUpload.toString())
MainPage.clickElement(findTestObject('Main/btnSubmit'))

if(MainPage.elementPresent(findTestObject('Main/dlgImport'))) {
	if(MainPage.elementPresent(findTestObject('Main/lblDialogText'))) {
		uploadMessage = WebUI.getText(findTestObject('Main/lblDialogText'))
		println(uploadMessage)
		if(uploadMessage == importMessage) {
			println('uploadMessage == importMessage')
			MainPage.clickElement(findTestObject('Main/btnDialogOK'))
			csvFile.delete()
		}else {
			println('uploadMessage != importMessage')
		}
	}else {
		
	}	
}


'Step 6 - Navigate to Employee List'
pageName = 'Employee List'
pageTitle = 'Employee Information'
MainPage.accessSubPageSuccessful(pageName, pageTitle, isActionPage)

strFull = strFullName[0] + '  ' + strFullName[1] + '  ' + strFullName[2]
WebUI.setText(findTestObject('Main/txtFieldName', [('labelName') : fieldName]), strFullName[0])
isAutoExpand = true
MainPage.selectFromDropdown(fieldName, strFull, isAutoExpand)

WebUI.click(findTestObject('Main/btnSubmit'))

if(EmployeePage.deleteNewEmployee(strFullName)) {
	println('delete new employee success')
}else {
	println('delete new employee not success')
}


'Step 7 - Close the browser'
WebUI.closeBrowser()



