package com.myleakyconlondon.util;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Elizabeth
 * Date: 14/07/13
 * Time: 12:32
 */
public class ValidationHelper {

    private List<String> messages;

    public void setMessages(List<String> messages) {
        this.messages = messages;
    }

    public List<String> getMessages() {

        return messages;
    }

    public ValidationHelper() {
        messages = new ArrayList<String>();
    }

    public boolean validateDay(String date, String dayNumber) {

         if(isDateValid(date) && isDayNumberValid(dayNumber)) {
             return true;
         }
        return false;
    }

    private boolean isDateValid(String date) {

        if(!date.trim().equals("Add New Day")) {
            return true;
        }
        messages.add("Date is required");
        return false;
    }

    private boolean isDayNumberValid(String dayNumber) {

        boolean isValid = false;
        if(dayNumber != null && dayNumber != "") {
            try {
                Integer.parseInt(dayNumber.trim());
                isValid = true;
            } catch (NumberFormatException e) {
                messages.add("Date number must be an integer");
            }
            messages.add("Date number is required");
        }
          return isValid;
    }

    public boolean validateEvent(String title, String startDate, String endDate)
    {
        if(isTitleValid(title))   {
            return true;
        }
        return false;
    }

    private boolean isTitleValid(String title) {

        if(title == null || title == "") {
            messages.add("Title is required");
            return false;
        }
        return true;
    }
}
