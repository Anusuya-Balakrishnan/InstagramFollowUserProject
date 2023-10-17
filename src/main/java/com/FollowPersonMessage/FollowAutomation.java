package com.FollowPersonMessage;
import java.io.InputStream;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
 
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
		
//		username Element
		WebElement userNameElement=wait.until(ExpectedConditions.presenceOfElementLocated
				(ByXPath.xpath("//input[@name='username']")));
		userNameElement.sendKeys(username);
		
//		password element
		WebElement passwordElement=wait.until(ExpectedConditions.presenceOfElementLocated
(ByXPath.xpath("//input[@name='password']")));
		passwordElement.sendKeys(password);
		
//		login button
		WebElement submitElement=wait.until(ExpectedConditions.elementToBeClickable
(ByXPath.xpath("//*[contains(text(),'Log in')]")));
		
		if(submitElement.isEnabled()) {
			submitElement.click();
		}
		else {
			System.out.println("submitElement is not enabled"); 
		}
		

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
//		not now button2
		WebElement NotNowElement2=wait.until(ExpectedConditions.
				elementToBeClickable(ByXPath.xpath("//*[contains(text(),'Not Now')]")));
		if(NotNowElement2.isEnabled()) {
			NotNowElement2.click();
		}
		else {
			System.out.println("NotNowElement2 is not enabled"); 
		}
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
		
		//logout part
		
//		WebElement moreButton=wait.until(ExpectedConditions.presenceOfElementLocated(
//				By.xpath("//span[text()='More']")));
		WebElement moreButton=wait.until(ExpectedConditions.presenceOfElementLocated(
				By.xpath("//div[@class='xdy9tzy']/following-sibling::span[1]")));
		moreButton.click();
		WebElement logoutButton=wait.until(ExpectedConditions.presenceOfElementLocated(
				By.xpath("//span[text()='Log out']")));
		logoutButton.click();
//		quit browser
		driver.quit();

		
	}


	
	public void sendMessage(RemoteWebDriver driver, WebDriverWait wait,String personName,String message) throws InterruptedException {
		

//	    finding direct message element
		WebElement messageButtonElement=wait.until(ExpectedConditions.presenceOfElementLocated(
				By.xpath("//a[contains(@aria-label,'Direct messaging')]")));
		
		messageButtonElement.click();
		
		
//		click send message button
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(20));
		WebElement sendMessageBtnElement=wait.until(ExpectedConditions.presenceOfElementLocated(
				By.xpath("//div[text()='Send message']")));
		sendMessageBtnElement.click();

		
//		search input box
		
		WebElement searchBoxElement=wait.until(ExpectedConditions.presenceOfElementLocated(
				By.cssSelector("input[name='queryBox']")));
		
//		find people
//		String person="thamizh.HD";
		searchBoxElement.sendKeys(personName);
		
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(20));
		
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
		
	// click chat button
		
		WebElement chatButton=wait.until(ExpectedConditions.presenceOfElementLocated(
				By.xpath("//div[text()='Chat']")));
		
		chatButton.click();
		
		
//		find message box
		
		WebElement messageBox=wait.until(ExpectedConditions.presenceOfElementLocated(
				By.xpath("//div[@aria-label='Message']")));
		
		messageBox.sendKeys(message);
		

//		select message body
//		WebElement messageBodyElement=wait.until(ExpectedConditions.presenceOfElementLocated(
//				By.xpath("//div[contains(@aria-label,'Messages in conversation with')]")));
//		List<WebElement> sendContentElements=messageBodyElement.findElements(
//				By.xpath("//div[contains(@class,'x13dflua x19991ni')]"));
//		
//		System.out.println("before sending element"+sendContentElements.size());
//		int previousSize=sendContentElements.size();
//		sending image file
//		WebElement fileUploadButton=wait.until(ExpectedConditions.presenceOfElementLocated
//				(By.xpath("//input[contains(@class,'x1s85apg')]")));
//		String path="C:\\Users\\DELL 1\\eclipse-workspace\\InstagramAutomation\\video\\video.mp4";
//		fileUploadButton.sendKeys(path);
		
//		send button
		
		WebElement sendButtonElement=wait.until(ExpectedConditions.presenceOfElementLocated(
				By.xpath("//div[text()='Send']")));
		
		sendButtonElement.click();
		
//		WebElement currentContentElement=wait.until(sendContentElements.get(previousSize)));
//		Thread.sleep(5000);
		    
	

	}

public static void main(String[] args) throws InterruptedException {
	FollowAutomation f=new FollowAutomation();

	f.followAutomation();
	
}
}