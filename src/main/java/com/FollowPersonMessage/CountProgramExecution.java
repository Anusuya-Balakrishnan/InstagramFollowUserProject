package com.FollowPersonMessage;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CountProgramExecution {
	
public void readCount() {
	int maxLimit=0,currentCount=0;
	try {
        // Load the Excel file
        FileInputStream excelFile = new FileInputStream("programExecutionCount.xlsx");
        Workbook workbook = new XSSFWorkbook(excelFile);
     // Get the first sheet
        Sheet sheet = workbook.getSheet("Data");
        
        for(Row row:sheet) {
        	Cell cell=row.getCell(0);
        	String value=cell.getStringCellValue();
        	if(value.equals("currentCount")) {
        		currentCount=(int) (row.getCell(1).getNumericCellValue());
        	}
        	else if(value.equals("maximumLimit")) {
        		maxLimit=(int) (row.getCell(1).getNumericCellValue());
        	}
        }
        
//     // Get the current date and time
//        LocalDateTime now = LocalDateTime.now();
//        
//        // Assuming you have a timestamp from a previous event
//        LocalDateTime lastExecution = LocalDateTime.of(2023, 10, 18, 12, 0);
//
//        // Calculate the duration between lastExecution and now
//        Duration duration = Duration.between(lastExecution, now);
//
//        // Check if 24 hours have passed
//        if (duration.toHours() >= 24) {
//            System.out.println("24 hours or more have passed since the last execution.");
//        } else {
//            System.out.println("Less than 24 hours have passed since the last execution.");
//        }
//        
//        
//        
//        // Get the current date and time
//        LocalDateTime currentDateTime = LocalDateTime.now();
//
//        // Define a format for the date
//        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
//
//        // Define a format for the time in railway time format
//        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
//
//        // Format the date and time as strings
//        String currentDate = currentDateTime.format(dateFormatter);
//        String railwayTime = currentDateTime.format(timeFormatter);
//
//        // Print the current date and railway time
//        System.out.println("Current Date: " + currentDate);
//        System.out.println("Railway Time: " + railwayTime);
     // Close the workbook
        workbook.close();
	}
	catch(Exception e) {
		
	}
	System.out.println("maxLimit"+maxLimit);
	System.out.println("currentCount"+currentCount);
	int changeValue=currentCount+1;
	if(currentCount<=maxLimit) {
		
		System.out.println("condition true");
		writeCount();
	}
	else {
		System.out.println("reacted maximum limit");
		writeCount();
	}
}
public boolean writeCount() {
		
		boolean result=false;
		
		int maxLimit=0,currentCount=0;
		try {
	        FileInputStream excelFile = new FileInputStream("userDetails.xlsx");
	        Workbook workbook = new XSSFWorkbook(excelFile);
	        Sheet sheet = workbook.getSheet("count");
	     
	        LocalDateTime lastExecutionDateTime = null;
	        for(Row row:sheet) {
	        	if(row.getCell(0).getStringCellValue().equals("executeStartTime")) {
//	        		lastExecutionDateTime = LocalDateTime.of(2023, 10, 18, 12, 0);
	        		lastExecutionDateTime=row.getCell(1).getLocalDateTimeCellValue();
	        		System.out.println(lastExecutionDateTime);
	        		
	        	}
	        	else if(row.getCell(0).getStringCellValue().equals("currentCount")) {
	        		currentCount=(int) (row.getCell(1).getNumericCellValue());
	        	}
	        	else if(row.getCell(0).getStringCellValue().equals("maximumLimit")) {
	        		maxLimit=(int) (row.getCell(1).getNumericCellValue());
	        	}
	        }
	        LocalDateTime now = LocalDateTime.now();
	     // Assuming you have a timestamp from a previous event
//	        LocalDateTime lastExecution = LocalDateTime.of(2023, 10, 18, 12, 0);

	        // Calculate the duration between lastExecution and now
	        Duration duration = Duration.between(lastExecutionDateTime, now);
	        
	        Row rowToUpdate = sheet.getRow(1);
	        Cell cellUpdate=rowToUpdate.getCell(1);// 0 is the row index (1st row)
	        
	        
	        // Check if 24 hours have passed
	        if (duration.toHours() > 24) {
	        	sheet.getRow(0).getCell(1).setCellValue(now);
	        	cellUpdate.setCellValue(1);
	        	System.out.println("24 hours more");
	        	result=true;
	            
	        } else {
	        	if(currentCount<maxLimit) {

	                if(rowToUpdate.getCell(0).getStringCellValue().equals("currentCount")) {
	                	cellUpdate.setCellValue(currentCount+1);
	                	System.out.println("Less than 24 hours");
	                	result=true;
	                }
	        	}
	        	else {
	        		cellUpdate.setCellValue(currentCount+1);
	        		System.out.println("reacted maximum limit");
	        		result=false;
	        		
	        	}
	            
	        }

	        System.out.println("maxLimit"+maxLimit);
	        System.out.println("currentCount"+currentCount);
	     // Save the changes
	        FileOutputStream fileOut = new FileOutputStream("userDetails.xlsx");
	        workbook.write(fileOut);
	        fileOut.close();
	     // Close the workbook
	        workbook.close();

		}
		catch(Exception e) {
			
		}
		return result;
	}
	

}
