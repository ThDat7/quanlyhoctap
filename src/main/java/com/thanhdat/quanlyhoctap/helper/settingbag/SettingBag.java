package com.thanhdat.quanlyhoctap.helper.settingbag;

import com.thanhdat.quanlyhoctap.entity.Setting;

import java.util.List;

public class SettingBag {
    private List<Setting> listSettings;

    public SettingBag(List<Setting> listSettings) {
        this.listSettings = listSettings;
    }

    protected Setting get(String key) {
        int index = listSettings.indexOf(new Setting(key));

        if (index >= 0) return listSettings.get(index);
        return null;
    }

    public String getValue(String key) {
        Setting setting = get(key);

        if (setting != null) return setting.getValue();
        throw new RuntimeException("Setting not found");
    }

    public List<Setting> list() {
        return listSettings;
    }

    public void update(String key, String value) {
        Setting setting = get(key);

        if (setting != null)
            throw new RuntimeException("Setting not found");
        else if (value == null)
            throw new RuntimeException("Value cannot be null");
        else setting.setValue(value);
    }
}
