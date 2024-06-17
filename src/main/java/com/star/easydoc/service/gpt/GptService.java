package com.star.easydoc.service.gpt;

import java.util.Map;
import java.util.Objects;

import com.google.common.collect.ImmutableMap;
import com.star.easydoc.common.Consts;
import com.star.easydoc.config.EasyDocConfig;
import com.star.easydoc.service.gpt.impl.ChatGlm3GptSupplier;
import com.star.easydoc.service.gpt.impl.ChatGlm4GptSupplier;
import com.star.easydoc.service.gpt.impl.DeepSeekCoderV2GptSupplier;
import org.apache.commons.lang3.StringUtils;

/**
 * @author wangchao
 * @date 2024/03/05
 */
public class GptService {

    private EasyDocConfig config;
    private Map<String, GptSupplier> gptSupplierMap;
    private static final Object LOCK = new Object();

    /**
     * 初始化
     *
     * @param config 配置
     */
    public void init(EasyDocConfig config) {
        if (gptSupplierMap != null && this.config != null) {
            return;
        }
        synchronized (LOCK) {
            if (gptSupplierMap != null && this.config != null) {
                return;
            }
            gptSupplierMap = ImmutableMap.<String, GptSupplier>builder()
                .put(Consts.CHATGLM_GPT_4, new ChatGlm4GptSupplier().init(config))
                .put(Consts.CHATGLM_GPT_3, new ChatGlm3GptSupplier().init(config))
                .put(Consts.DEEP_SEEK_CODER_V2, new DeepSeekCoderV2GptSupplier().init(config))
                .build();
            this.config = config;
        }
    }

    /**
     * 聊天
     *
     * @param content 消息
     * @return {@code String}
     */
    public String chat(String content) {
        GptSupplier translator = gptSupplierMap.get(config.getTranslator());
        if (Objects.isNull(translator)) {
            return StringUtils.EMPTY;
        }
        return translator.chat(content);
    }

}
