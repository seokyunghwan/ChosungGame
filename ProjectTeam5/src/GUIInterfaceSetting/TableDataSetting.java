package GUIInterfaceSetting;

import java.text.DecimalFormat;
import java.util.StringJoiner;

import VO.RoomData;

public class TableDataSetting {
	
	private DecimalFormat fmt = new DecimalFormat("000");
	private String[] columnNames = new String[] {"", "", ""};
	private String nbsp = "&nbsp&nbsp&nbsp";
	private StringJoiner joiner;
	private Object[] sendData;
	
	public Object[][] getEmptyData() {
		Object[][] emptyObject = 
			{
					{"", "", ""}
			};
		return emptyObject;
	}
	
	public String[] getColumnNames() {
		return columnNames;
	}
	
	public Object[] getRowData(RoomData roomData) {
		
		if (roomData != null) {
			
			sendData = new Object[3];
			
			joiner = new StringJoiner("<br>", "<html><center>", "</center></html>");
			joiner.add("방 번호");
			joiner.add(fmt.format(roomData.getRoomNumber()));
			sendData[0] = joiner.toString();
			
			joiner = new StringJoiner("<br>", "<html>", "</html>");
			joiner.add(nbsp + "방 제목 : " + roomData.getRoomTitle());
			String str1 = nbsp + "방 인원 : " + roomData.getRoomUserNameList().size() + "/" + roomData.getRoomLimit() + nbsp;
			String str2 = "방장 : " + roomData.getRoomOwner() + nbsp;
			String str3;
			if (roomData.isPrivate())
				str3 = "비공개방";
			else
				str3 = "공개방";
			joiner.add(str1 + str2 + str3);
			sendData[1] = joiner.toString();
			
			sendData[2] = "입장하기";
			
			return sendData;
		} else
			return null;
		
	}
}
