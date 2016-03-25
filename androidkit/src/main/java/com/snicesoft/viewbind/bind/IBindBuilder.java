package com.snicesoft.viewbind.bind;

import com.snicesoft.viewbind.annotation.DataBind;

/**
 * Created by zhuzhe on 16/3/25.
 */
public class IBindBuilder {
    public static IBind create(DataBind dataBind) {
        IBind bind = null;
        switch (dataBind.dataType()) {
            case TEXT:
                bind = new BindText(dataBind);
                break;
            case CHECK:
                bind = new BindCheck(dataBind);
                break;
            case IMG:
                bind = new BindImg(dataBind);
                break;
            case HTML:
                bind = new BindHtml(dataBind);
                break;
            case ADAPTER:
                bind = new BindAdapter(dataBind);
                break;
            case PROGRESS:
                bind = new BindProgress(dataBind);
                break;
            case RATING:
                bind = new BindRating(dataBind);
                break;
            default:
                bind = null;
                break;
        }
        return bind;
    }
}
