package com.springboot.Helper;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
//import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import com.springboot.Entity.User;

public class MyExcelHalper {

	public static String[] HEADER= {
			"Id",
			"Name",
			"Email",
			"PhoneNo",
			"Address"
	};
	
	public static String SHEET_NAME = "User_Data";
	
	
	//Check the multipart file is of excel type file or not
	public static boolean checkExcelFromat(MultipartFile file) {
		String contentType = file.getContentType();
		if(contentType.equals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")){
			return true;
		}
		return false;
	}
	
	//It convert excel to list of User
	public static List<User> convertExcelToListOfProduct(InputStream inputStream){
		List<User> list = new ArrayList<>();
		try {
			XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
			XSSFSheet sheet = workbook.getSheet("data");
			int rowNumber = 0;
			Iterator<Row> iterator = sheet.iterator();
			//Skip the first row (index 0)
			while(iterator.hasNext()) {
				Row row = iterator.next();
				if(rowNumber == 0) {
					rowNumber++;
					continue;
				}
				Iterator<Cell> cells = row.iterator();
				int cId = 0;
				User user =  new User();
				while(cells .hasNext()) {
					Cell cell = cells.next();
					
					 // Skip the first column (index 0)
	                if (cId == 0) {
	                    cId++;
	                    continue;
	                }
	                
					switch(cId) {
//					case 0:
//						user.setId((int)cell.getNumericCellValue());
//						break;
					case 1:
						user.setName(cell.getStringCellValue());
						break;
					case 2:
						user.setEmail(cell.getStringCellValue());
						break;
					case 3:
						 if (cell.getCellType() == CellType.STRING) {
	                            user.setphoneNo(cell.getStringCellValue());
	                        } else if (cell.getCellType() == CellType.NUMERIC) {
	                            user.setphoneNo(String.valueOf((long) cell.getNumericCellValue()));
	                        }
						break;
					case 4:
						user.setAddress(cell.getStringCellValue());
						break;
					default:
						break;
					}
					cId++;
				}
				list.add(user);
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	public static ByteArrayInputStream dataToExcel(List<User> list) throws IOException {
		Workbook workbook = new XSSFWorkbook();
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		try {
			//Create Sheet
			Sheet sheet = workbook.createSheet(SHEET_NAME);
			
			//Create row: Header row
			Row row = sheet.createRow(0);
			for(int i=0; i<HEADER.length; i++) {
				Cell cell = row.createCell(i);
				cell.setCellValue(HEADER[i]);
			}
			
			//Value row
			int rowIndex = 1;
			for(User us : list) {
				Row dataRow = sheet.createRow(rowIndex);
				dataRow.createCell(0).setCellValue(us.getId());
				dataRow.createCell(1).setCellValue(us.getName());
				dataRow.createCell(2).setCellValue(us.getEmail());
				dataRow.createCell(3).setCellValue(us.getphoneNo());
				dataRow.createCell(4).setCellValue(us.getAddress());
				rowIndex++;
			}
			workbook.write(stream);
			return new ByteArrayInputStream(stream.toByteArray());
		}catch(IOException e) {
			e.printStackTrace();
			System.out.println("Error");
			return null;
		}finally {
			workbook.close();
			stream.close();
		}
		
	}
}
