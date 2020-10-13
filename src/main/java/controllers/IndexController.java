package controllers;

import java.util.List;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;


@RestController
public class IndexController{
	HashMap<Integer, DayInfo> map;
	
	private class DayInfo{
		public boolean isAvailable;
		public int addDaysToReachNext;
	}
	
	private int[] merge(int[] A){
        if(A.length == 1){
            return A;
        }
        
        int leftSize = A.length / 2;
        int rightSize = A.length / 2;
        if(leftSize+rightSize < A.length){
            rightSize += 1;
        }
        
        int[] leftArray = new int[leftSize];
        int[] rightArray = new int[rightSize];
        
        for(int i=0;i<A.length;i++){
            if(i<leftArray.length){
                leftArray[i] = A[i];
            }else{
                rightArray[i - leftArray.length] = A[i];
            }
        }
        
        int[] leftSorted = merge(leftArray);
        int leftCursor = 0;
        int[] rightSorted = merge(rightArray);
        int rightCursor = 0;
        
        int[] newSorted = new int[leftSorted.length + rightSorted.length];
        int newSortedCursor = 0;
        for(int i=0;i<newSorted.length;i++){
            newSortedCursor = i;
            if(leftCursor == leftSorted.length || rightCursor == rightSorted.length){
                break;
            }
            
            if(leftSorted[leftCursor] < rightSorted[rightCursor]){
                newSorted[i] = leftSorted[leftCursor];
                leftCursor++;
            }else{
                newSorted[i] = rightSorted[rightCursor];
                rightCursor++;
            }
        }
        
        if(leftCursor < leftSorted.length){
            for(int i=leftCursor;i<leftSorted.length;i++){
                newSorted[newSortedCursor] = leftSorted[i];
                newSortedCursor++;
            }
        }else if (rightCursor < rightSorted.length){
            for(int i=rightCursor;i<rightSorted.length;i++){
                newSorted[newSortedCursor] = rightSorted[i];
                newSortedCursor++;
            }
        }
        
        return newSorted;
    }
	
	private void setup(int[] days) {
		map = new HashMap<Integer, DayInfo>();
		for(int i=0;i<7;i++) {
			DayInfo initDayInfo = new DayInfo();
			initDayInfo.addDaysToReachNext = -1;
			initDayInfo.isAvailable = false;
			map.put(i, initDayInfo);
		}
		
		int[] sortedDays = merge(days);
		int initialElement = 0;
		int lastElement = 0;
		for(int i=0;i<sortedDays.length;i++) {
			if(sortedDays[i] > 6) {
				break;
			}else {
				DayInfo currentDayInfo = map.get(sortedDays[i]);
				//System.out.println(sortedDays[i]);
				currentDayInfo.isAvailable = true;
				if(i > 0) {
					lastElement = sortedDays[i];
					if(sortedDays[i] - sortedDays[i-1] > 0) {
						DayInfo previousDayInfo = map.get(sortedDays[i-1]);
						previousDayInfo.addDaysToReachNext = sortedDays[i] - sortedDays[i-1];
						map.replace(sortedDays[i-1], previousDayInfo);
					}
					map.replace(sortedDays[i], currentDayInfo);
				}else {
					initialElement = lastElement = sortedDays[i];
					map.replace(sortedDays[i], currentDayInfo);
				}
			}
		}
		
		DayInfo lastDayInfo = map.get(lastElement);
		lastDayInfo.addDaysToReachNext = 7 - (lastElement - initialElement);
		map.replace(lastElement, lastDayInfo);
	}

	@GetMapping("/list-days")
    public ResponseEntity<String[]> listDays(@RequestParam(name="date",required = true) Date date,
    		@RequestParam(name="days",required = true) int[] days,
    		@RequestParam(name="chapterDays",required = true) int chapterDays) {
		int chaptersToFinish = 30;
		int daysNeeededToFinishChapter = chapterDays * chaptersToFinish;
		String[] datesList = new String[daysNeeededToFinishChapter];
		
		setup(days);
		
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
		dayOfWeek = dayOfWeek % 7;
		
		String firstDateToTake = "";
		System.out.println(map.get(6).isAvailable);
		if(map.get(dayOfWeek).isAvailable) {
			firstDateToTake = String.valueOf(c.get(Calendar.YEAR))+"-"
					+String.valueOf(c.get(Calendar.MONTH) + 1)+"-"
					+String.valueOf(c.get(Calendar.DAY_OF_MONTH));
            datesList[0] = firstDateToTake;
		}else {
			int count = 0;
			while(count<7) {
				c.add(Calendar.DAY_OF_MONTH, 1);
				dayOfWeek = c.get(Calendar.DAY_OF_WEEK) % 7;
				if(map.get(dayOfWeek).isAvailable) {
					firstDateToTake = String.valueOf(c.get(Calendar.YEAR))+"-"
							+String.valueOf(c.get(Calendar.MONTH) + 1)+"-"
							+String.valueOf(c.get(Calendar.DAY_OF_MONTH));
					datesList[0] = firstDateToTake;
					break;
					
				}
				count++;
			}
		}
		
		if(!firstDateToTake.equals("")) {
			for(int i=1;i<daysNeeededToFinishChapter;i++) {
				DayInfo currentDayInfo = map.get(dayOfWeek);
				c.add(Calendar.DAY_OF_MONTH, currentDayInfo.addDaysToReachNext);
				dayOfWeek = c.get(Calendar.DAY_OF_WEEK) % 7;
				datesList[i] = String.valueOf(c.get(Calendar.YEAR))+"-"
						+String.valueOf(c.get(Calendar.MONTH) + 1)+"-"
						+String.valueOf(c.get(Calendar.DAY_OF_MONTH));
			}
		}
		
		ResponseEntity<String[]> res = new ResponseEntity<String[]>(datesList,HttpStatus.OK);
		
		
    	return res;
    }
}
