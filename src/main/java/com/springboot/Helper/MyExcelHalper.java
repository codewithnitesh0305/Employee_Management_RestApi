package com.springboot.Helper;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
//import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import com.springboot.Entity.User;

public class MyExcelHalper {

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
}
