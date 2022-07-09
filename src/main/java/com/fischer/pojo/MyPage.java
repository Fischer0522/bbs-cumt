package com.fischer.pojo;


import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author fischer
 */
@Getter
@NoArgsConstructor
public class MyPage {
    private static final int MAX_LIMIT=100;
    private int offset = 0;
    private int limit = 20;
    public MyPage(int offset, int limit){
        setOffset(offset);
        setLimit(limit);
    }
    private void setOffset(int offset)
    {
        if(offset>0)
        {
            this.offset=offset;;
        }
    }
    private void setLimit(int limit){
        if(limit>MAX_LIMIT){
            this.limit=MAX_LIMIT;
        }
        else if(limit>0){
            this.limit=limit;
        }
    }

}
