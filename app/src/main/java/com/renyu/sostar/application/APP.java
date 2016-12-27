package com.renyu.sostar.application;

import com.tencent.tinker.loader.app.TinkerApplication;
import com.tencent.tinker.loader.shareutil.ShareConstants;

/**
 * Created by renyu on 2016/12/26.
 */

public class APP extends TinkerApplication {

    public APP() {
        super(ShareConstants.TINKER_ENABLE_ALL, "com.renyu.sostar.application.APPLike",
                "com.tencent.tinker.loader.TinkerLoader", false);
    }
}
