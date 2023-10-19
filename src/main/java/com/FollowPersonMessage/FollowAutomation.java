package com.FollowPersonMessage;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.By.ByXPath;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.github.bonigarcia.wdm.WebDriverManager;

public class FollowAutomation {

	
	public void followAutomation() throws InterruptedException {
		
//		login page
		WebDriverManager.chromedriver().setup();
		RemoteWebDriver driver=new ChromeDriver();
		driver.manage().window().maximize();
//		10 second wait
		Thread.sleep(10000);
		driver.get("https://www.instagram.com/");
		
//		create object for excelExample
		
		ExcelExample excelobj=new ExcelExample();
		
//		get user data from excel file
		UserInformation userData=excelobj.readUserDetails();
		
		String username=userData.username;
		String password=userData.password;
		String message=userData.sendMessage;
		System.out.println("message"+message);
//		explicit wait
		
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(50));
		
		CountProgramExecution c=new CountProgramExecution();
		
//		return true or false based on time and count of execution of program
		if(!c.writeCount()) {
			driver.close();
		}
		else {
			

//		username Element
		WebElement userNameElement=wait.until(ExpectedConditions.presenceOfElementLocated
				(ByXPath.xpath("//input[@name='username']")));
		userNameElement.sendKeys(username);
		
//		wait 5 second
		Thread.sleep(5000);
//		password element
		WebElement passwordElement=wait.until(ExpectedConditions.presenceOfElementLocated
(ByXPath.xpath("//input[@name='password']")));
		passwordElement.sendKeys(password);
		
//		10 second wait
		Thread.sleep(10000);
//		login button
		WebElement submitElement=wait.until(ExpectedConditions.elementToBeClickable
(ByXPath.xpath("//*[contains(text(),'Log in')]")));
		
		if(submitElement.isEnabled()) {
			submitElement.click();
		}
		else {
			System.out.println("submitElement is not enabled"); 
		}
		

//		wait 5 second
		Thread.sleep(5000);
//		home page
//		not now button1 
		WebElement NotNowElement1=wait.until(ExpectedConditions.elementToBeClickable
				(ByXPath.xpath("//*[contains(text(),'Not Now')]")));
		if(NotNowElement1.isEnabled()) {
			NotNowElement1.click();
		}
		else {
			System.out.println("NotNowElement1 is not enabled"); 
		}
		
//		wait 5 second
		Thread.sleep(5000);
//		not now button2
		WebElement NotNowElement2=wait.until(ExpectedConditions.
				elementToBeClickable(ByXPath.xpath("//*[contains(text(),'Not Now')]")));
		if(NotNowElement2.isEnabled()) {
			NotNowElement2.click();
		}
		else {
			System.out.println("NotNowElement2 is not enabled"); 
		}
		
//		wait 5 second
		Thread.sleep(5000);
//		notification element

		WebElement notificationElement=wait.until(ExpectedConditions.
				presenceOfElementLocated(By.xpath("//span[text()='Notifications']")));
		if(notificationElement.isEnabled()) {
			notificationElement.click();
		}else {
			System.out.println("notification element is not present");
		}
		
		WebElement todayElement=wait.until(ExpectedConditions.
				presenceOfElementLocated(By.xpath("(//div[contains(@class,'x78zum5 x1c436fg')]//span)[1]")));
		
//		wait 5 second
		Thread.sleep(5000);
//		find element which contains started following you 
		List<WebElement> elements=wait.until(
				ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("//span[contains(text(),'started following you.')]")));
		
		ArrayList<String> personList=new ArrayList<String>();
		
		System.out.println("size"+elements.size());
		
		for(int i=0;i<elements.size();i++) {

			String []userUrl=elements.get(i).getText().split("\n");

			personList.add(userUrl[0]);
		}
		System.out.println(personList);
		
		ArrayList<String> existingFollowPerson=excelobj.readFollowPersonData();
		ArrayList<String> newPersonList=new ArrayList<String>();
		for(String newEachPerson:personList) {
			int count=0;
			for(String existingEachPerson:existingFollowPerson) {
				if(existingEachPerson.equals(newEachPerson)) {
					count++;
					break;
				}
			}
			if(count==0) {
				newPersonList.add(newEachPerson);
			}
		}
		
		for(String eachPerson:newPersonList) {
//			System.out.println(eachPerson);
			sendMessage(driver,wait,eachPerson,message);
		}
		
//		writing data in excel sheet
		excelobj.writeFollowPersonData(newPersonList);
		
		
//		wait 5 second
		Thread.sleep(5000);
		//logout part
		
//		WebElement moreButton=wait.until(ExpectedConditions.presenceOfElementLocated(
//				By.xpath("//span[text()='More']")));
		WebElement moreButton=wait.until(ExpectedConditions.presenceOfElementLocated(
				By.xpath("//div[@class='xdy9tzy']/following-sibling::span[1]")));
		moreButton.click();
		
//		wait 5 second
		Thread.sleep(5000);
		WebElement logoutButton=wait.until(ExpectedConditions.presenceOfElementLocated(
				By.xpath("//span[text()='Log out']")));
		logoutButton.click();
		
//		10 second wait
		Thread.sleep(10000);
//		quit browser
		driver.quit();
		}
		
	}


	
	public void sendMessage(RemoteWebDriver driver, WebDriverWait wait,String personName,String message) throws InterruptedException {
		
//		wait 5 second
		Thread.sleep(5000);
//	    finding direct message element
		WebElement messageButtonElement=wait.until(ExpectedConditions.presenceOfElementLocated(
				By.xpath("//a[contains(@aria-label,'Direct messaging')]")));
		
		messageButtonElement.click();
		
//		wait 5 second
		Thread.sleep(5000);
//		click send message button
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(20));
		WebElement sendMessageBtnElement=wait.until(ExpectedConditions.presenceOfElementLocated(
				By.xpath("//div[text()='Send message']")));
		sendMessageBtnElement.click();

//		wait 5 second
		Thread.sleep(5000);
//		search input box
		
		WebElement searchBoxElement=wait.until(ExpectedConditions.presenceOfElementLocated(
				By.cssSelector("input[name='queryBox']")));
		
//		find people
//		String person="thamizh.HD";
		searchBoxElement.sendKeys(personName);
		
//		wait 5 second
		Thread.sleep(5000);
//		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(20));
		
		try {
			String personXpath="//span[contains(text(),'".concat(personName.toLowerCase()).concat("')]");
			WebElement searchPeopleElement=driver.findElement(By.xpath(personXpath));
			searchPeopleElement.click();
		}
		catch(Exception e) {
//			get check box name
			WebElement labelCheckBoxElement=driver.findElement(By.xpath("//label[contains(@for,'ContactSearchResultCheckbox')]"));
			WebElement searchResultCheckBox=labelCheckBoxElement.findElement(
					By.xpath("(//input[contains(@name,'ContactSearchResultCheckbox')])[1]"));
					
			searchResultCheckBox.click();
		}
		
//		wait 5 second
		Thread.sleep(5000);
	// click chat button
		
		WebElement chatButton=wait.until(ExpectedConditions.presenceOfElementLocated(
				By.xpath("//div[text()='Chat']")));
		
		chatButton.click();
		
//		wait 5 second
		Thread.sleep(5000);
//		find message box
		
		WebElement messageBox=wait.until(ExpectedConditions.presenceOfElementLocated(
				By.xpath("//div[@aria-label='Message']")));
		
		messageBox.sendKeys(message);
		
//		wait 5 second
		Thread.sleep(5000);
				
//		send button
		
		WebElement sendButtonElement=wait.until(ExpectedConditions.presenceOfElementLocated(
				By.xpath("//div[text()='Send']")));
		
		sendButtonElement.click();
		Thread.sleep(5000);

//		WebElement currentContentElement=wait.until(sendContentElements.get(previousSize)));
//		Thread.sleep(5000);
		    
	

	}

public static void main(String[] args) throws InterruptedException {
	FollowAutomation f=new FollowAutomation();

	f.followAutomation();
	
}
}