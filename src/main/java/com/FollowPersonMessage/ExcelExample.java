package com.FollowPersonMessage;
import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

//import LikePackage.UserDetailsClass;

public class ExcelExample {
	
	public UserInformation readUserDetails() {
		String currentDirectory = System.getProperty("user.dir");
        String fileName = currentDirectory + File.separator + "userDetails.xlsx";
//		String fileName="userDetails.xlsx";
		String username=null;
		String password = null;
		String sendMessage=null;
		String linkMessage=null;
//		ArrayList<String> postUrl=new ArrayList<String>();
			try (FileInputStream fileInputStream = new FileInputStream(fileName);
	                Workbook workbook = new XSSFWorkbook(fileInputStream)) {
					 // Specify the sheet name where your map data is located
		               Sheet sheet = workbook.getSheet("Data");
		               
		               Iterator<Row> rowIterator = sheet.iterator();
		               while (rowIterator.hasNext()) {
		                   Row row = rowIterator.next();
		                   Cell keyCell = row.getCell(0);//postName
		                   if(keyCell.getStringCellValue().equals("username")) {
		                	   username=row.getCell(1).getStringCellValue();
		                   }
		                   else if(keyCell.getStringCellValue().equals("password")) {
		                	   password=row.getCell(1).getStringCellValue();
		                   }
		                   
		                   else if(keyCell.getStringCellValue().equals("sendMessage")) {
		                	   sendMessage=row.getCell(1).getStringCellValue();
		                   }
		                   else if(keyCell.getStringCellValue().equals("linkMessage")) {
		                	   linkMessage=row.getCell(1).getStringCellValue();
		                	   
		                	   if(!linkMessage.isEmpty()) {
				            	   sendMessage=sendMessage.concat(" ").concat(linkMessage);
				               }
		                	   


		                   }
		                   
		               }
		               
		               

			

		}catch (IOException e) {
	        e.printStackTrace();
	    }
			UserInformation user=new UserInformation(username, password, sendMessage);
//			System.out.println(user);
		return user;

	}

	
	
	public ArrayList<String> readFollowPersonData() {
		ArrayList<String> existingPerson=new ArrayList<String>();
//		String excelFilePath="FollowData.xlsx";
		String currentDirectory = System.getProperty("user.dir");
        String excelFilePath = currentDirectory + File.separator + "FollowData.xlsx";
		
		try (FileInputStream fileInputStream = new FileInputStream(excelFilePath);
                Workbook workbook = new XSSFWorkbook(fileInputStream)) {

               // Specify the sheet name where your map data is located
               Sheet sheet = workbook.getSheet("Data");
               
               Iterator<Row> rowIterator = sheet.iterator();
               while (rowIterator.hasNext()) {
                   Row row = rowIterator.next();
                   Cell keyCell = row.getCell(0);//personName
                   

                   if (keyCell != null ) {
                       String personName = keyCell.getStringCellValue();//personName
                      
                       existingPerson.add(personName);
                   }
               }

           } catch (IOException e) {
               e.printStackTrace();
           }
System.out.println(existingPerson);
return existingPerson;
	}
	
	public  void writeFollowPersonData(ArrayList<String> personList) {
		// Specify the existing file you want to append data to
//        String existingFilePath = "FollowData.xlsx";
		String currentDirectory = System.getProperty("user.dir");
        String existingFilePath = currentDirectory + File.separator + "FollowData.xlsx";

        try (FileInputStream existingFileInputStream = new FileInputStream(existingFilePath);
             Workbook existingWorkbook = new XSSFWorkbook(existingFileInputStream)) {

            Sheet sheet = existingWorkbook.getSheet("Data");

            // Calculate the next available row
            int rowNum = sheet.getLastRowNum() + 1;

            
            // Add the new data to the existing sheet
            for (String eachPerson:personList) {
                Row row = sheet.createRow(rowNum++);
                Cell cell1 = row.createCell(0);
                cell1.setCellValue(eachPerson);
            }

            try (FileOutputStream outputStream = new FileOutputStream(existingFilePath)) {
                existingWorkbook.write(outputStream);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
	}

}
