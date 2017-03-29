package pro.kondratev.xlsxpoiexample;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.*;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.CellStyle;

public class read {
	Row row;
	CellStyle c1;
	Cell cell;
	String cellvalue;


	//Get the workbook instance for XLSX file
	HSSFWorkbook workbook;
	HSSFSheet sheet;
	int rowCount;
	int colCount;



	read (InputStream fis)
	{
		try{

			//Get the workbook instance for XLSX file
			workbook = new HSSFWorkbook(fis);
			sheet = workbook.getSheetAt(0);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}


	}

	public void returnCell(int r,int c)
	{
		row=sheet.getRow(r);
		cell= row.getCell(c);
		cellvalue=cell.getStringCellValue();
		c1=cell.getCellStyle();

	}


	public void readExcel(String fName)throws Exception
	{
		FirebaseDatabase db= FirebaseDatabase.getInstance();
		DatabaseReference myRef=db.getReference();
		Lecture l1=new Lecture();
		String days[]={"Monday","Tuesday","Wednesday","Thursday","Friday"};
		int r,c,tempc,tempc1,labLength;
		int lec=0;
		String lecName,room="",teacher="",branchName;
		int day=0;

		int rowCount=sheet.getPhysicalNumberOfRows();
		int colCount=sheet.getRow(6).getPhysicalNumberOfCells();
		for(c=2;c<(colCount-1);c=c+2)
		{
			r=5;
			returnCell(r,c);
			branchName=cell.getStringCellValue();
			r++;
			for(;r<(rowCount-1);r++)
			{
				labLength=0;
				if(lec==10)
				{
					lec=0;
					day++;
				}
				if(day==5)
					day=0;
				lec++;
				tempc1=c;
				returnCell(r,c);
				while(c1.getBorderLeft()!=CellStyle.BORDER_THICK&&c1.getBorderLeft()!=CellStyle.BORDER_THIN)
				{
					tempc1--;
					returnCell(r,tempc1);
				}

				if(cell.getStringCellValue()!="")

				{
					lecName=cell.getStringCellValue();
					r++;
					returnCell(r,c);
					if(cell.getStringCellValue()=="")
					{
						tempc=c;
						while(c1.getBorderLeft()!=CellStyle.BORDER_THIN&&c1.getBorderLeft()!=CellStyle.BORDER_THICK)
						{
							tempc--;
							returnCell(r, tempc);
						}
						room = cell.getStringCellValue();
						tempc=c;
						while(c1.getBorderRight()!=CellStyle.BORDER_THIN&&c1.getBorderRight()!=CellStyle.BORDER_THICK)
						{
							tempc++;
							returnCell(r, tempc);
						}
						teacher=cell.getStringCellValue();
					}
					else
					{
						if(c1.getBorderBottom()!=CellStyle.BORDER_THIN&&c1.getBorderBottom()!=CellStyle.BORDER_THICK) //LABCODE
						{
//	    					  room=cell.getStringCellValue();
//	    					  r=r+2;
//	    					  returnCell(r, c);
//	    					  teacher=cell.getStringCellValue();
//	    					  lec++;
							int tempr;
							tempr=r;
							labLength=2;
							tempr=tempr+2;
							returnCell(tempr,c);
							if(c1.getBorderBottom()!=CellStyle.BORDER_THIN&&c1.getBorderBottom()!=CellStyle.BORDER_THICK)
							{
								labLength=3;
								tempr=tempr+2;
								returnCell(tempr,c);
								if(c1.getBorderBottom()!=CellStyle.BORDER_THIN&&c1.getBorderBottom()!=CellStyle.BORDER_THICK)
									labLength=4;
							}
							switch(labLength)
							{
								case 2:
									room=cell.getStringCellValue();
									r=r+2;
									returnCell(r, c);
									teacher=cell.getStringCellValue();
									lec++;
									break;
								case 3:
									returnCell(r, c);
									room=cell.getStringCellValue();
									r=r+4;
									returnCell(r, c);
									teacher=cell.getStringCellValue();
									lec=lec+2;

									break;
								case 4:
									returnCell(r, c);
									room=cell.getStringCellValue();
									r=r+4;
									returnCell(r, c);
									teacher=cell.getStringCellValue();
									lec=lec+3;
									r=r+2;

									break;
							}
							labLength--;

						}
						else
						{
							if(c1.getBorderLeft()!=CellStyle.BORDER_THIN||c1.getBorderLeft()!=CellStyle.BORDER_THICK)
							{
								room = cell.getStringCellValue();
								tempc=c;
								//while(c1.getBorderRight()!=CellStyle.BORDER_THIN&&c1.getBorderRight()!=CellStyle.BORDER_THICK)
								while(c1.getBorderRight() == CellStyle.BORDER_NONE)
								{

									tempc++;
									returnCell(r, tempc);
								}
								teacher=cell.getStringCellValue();

							}
							else
							{
								teacher=cell.getStringCellValue();
								tempc=c;
								while(c1.getBorderLeft()!=CellStyle.BORDER_THIN&&c1.getBorderLeft()!=CellStyle.BORDER_THICK)
								{
									tempc--;
									returnCell(r, tempc);
								}
								room = cell.getStringCellValue();

							}
						}
					}
				}
				else
				{
					r++;
					lecName="No Lecture";
					room=null;
					teacher=null;
				}
				System.out.println(days[day]+" "+ (lec-labLength) + " "+branchName+"  "+"Lecture ="+lecName+" "+"Room ="+room+"TEacher ="+teacher);
				l1.setCCode(lecName);
				l1.setRoom(room);
				l1.setTeacher(teacher);
			myRef.child(fName).child(branchName).child(days[day]).child(String.valueOf(lec-labLength)).setValue(l1);

			}




		}

	}

}
