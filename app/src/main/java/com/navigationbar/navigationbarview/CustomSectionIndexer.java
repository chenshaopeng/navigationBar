package com.navigationbar.navigationbarview;

import android.util.Log;
import android.widget.SectionIndexer;

import com.navigationbar.navigationbarview.view.DataBean;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;


/**
 * Created by 陈韶鹏 on 2017/1/11.
 */

public class CustomSectionIndexer<T extends DataBean> implements SectionIndexer {

    private String[] mDataArray;
    private java.text.Collator mCollator;
    private String[] mGroupArray;
    private HashMap<String, Integer> mSaveMap = new HashMap<>();
    private String favouriteKey;

    public String[] getmDataArray() {
        return mDataArray;
    }

    public void setmDataArray(String[] mDataArray) {
        this.mDataArray = mDataArray;
    }

    public String[] getmGroupArray() {
        return mGroupArray;
    }

    public void setmGroupArray(String[] mGroupArray) {
        this.mGroupArray = mGroupArray;
    }

    public CustomSectionIndexer(String[] data, String[] groupArray) {
        mDataArray = data;
        mGroupArray = groupArray;

        // Get a Collator for the current locale for string comparisons.
  /*      mCollator = java.text.Collator.getInstance();
        mCollator.setStrength(java.text.Collator.PRIMARY);*/
    }

    @Override
    public Object[] getSections() {
        return mDataArray;
    }

    /**
     * Performs a binary search or cache lookup to find the first row that
     * matches a given section's starting letter.
     * @param sectionIndex the section to search for
     * @return the row index of the first occurrence, or the nearest next letter.
     * For instance, if searching for "T" and no "T" is found, then the first
     * row starting with "U" or any higher letter is returned. If there is no
     * data following "T" at all, then the list size is returned.
     */
    @Override
    public int getPositionForSection(int sectionIndex) {

        if (mDataArray == null || mGroupArray == null) {
            return 0;
        }
        // Check bounds
        if (sectionIndex <= 0) {
            return 0;
        }
        if (sectionIndex >= mGroupArray.length) {
            sectionIndex = mGroupArray.length - 1;
        }

        int count = mDataArray.length - 1;
        int start = 0;
        int end = count;
        int pos;
        String targetLetter = mGroupArray[sectionIndex];

        String key = targetLetter;

        // Do we have the position of the previous section?
        //forbidden index cache, when update data may cause index changed but cache haven't sync changed.
      /*  if (sectionIndex > 0) {
            String prevLetter = mGroupArray[sectionIndex - 1];
            Integer integer = mSaveMap.get(prevLetter);
            if (integer != null) {
                start = Math.abs(integer);
            }
        }*/
        pos = (start + end) / 2;
        while (pos < end) {
            String currentLetter = mDataArray[pos];

            int diff = compare(currentLetter, targetLetter);
            if (diff != 0) {

                if (diff < 0) {
                    start = pos + 1;
                    if (start >= count) {
                        pos = count;
                        break;
                    }
                } else {
                    end = pos;
                }

            } else {
                if (start == pos) {
                    break;
                } else {
                    end = pos;
                }
              /*  if (pos == 0) {
                    break;
                } else {
                    int i = pos;
                    while(--i > 0) {
                        int result = compare(mDataArray[i], targetLetter);
                        if (result != 0) {
                            pos = i+1;
                            break;
                        }
                    }
                    break;
                }*/
            }
            pos = (start + end) / 2;
        }
        //while loop end
//        mSaveMap.put(key, pos);
        return pos;
    }

    /**
     * Returns the section index for a given position in the list by querying the item
     * and comparing it with all items in the section array.
     */
    public int getSectionForPosition(int position) {
        String bandAtPosion = mDataArray[position];

        for (int i=0; i< mGroupArray.length; i++) {
            String targetLetter = mGroupArray[i];
            if (compare(bandAtPosion, targetLetter) == 0) {
                return i;
            }
        }
        return 0; // Don't recognize the letter - falls under zero'th section
    }


    /**
     * Default implementation compares the first character of word with letter.
     */
    protected int compare(String word, String letter) {
        final String firstLetter;
        if (word.length() == 0) {
            firstLetter = " ";
        } else {
            firstLetter = word.substring(0, 1);
        }
        return firstLetter.compareTo(letter);
    }

    public  void sort(List<T> list) {
        Collections.sort(list, myComparator);
    }

    Comparator<T> myComparator = new Comparator<T>(){
        @Override
        public int compare(T t, T t1) {
            return t.getSortKey().compareTo(t1.getSortKey());
        }
    };

    //for translate character  "#" to "|"
    private String translateCharacter(String title) {
        String substring = title.substring(0, 1).toUpperCase();
        if (substring.matches("[A-Z]")) {
            return substring;
        }
        //add check character "#"
        if (substring.equals(favouriteKey)) {
            return "0";
        }

        return "|";
    }
    //add favourite and #


    public void setFavouriteKey(String favouriteKey) {
        this.favouriteKey = favouriteKey;
    }

    public int getFirstSectionPosition(int position) {
        return getPositionForSection(getSectionForPosition(position));
    }

    public String getPositionChart(int position) {
        Log.d("chen","position: " + position);
        Log.d("chen","first Section Position: " + getSectionForPosition(position));
        return mGroupArray[getSectionForPosition(position)];
    }

}
