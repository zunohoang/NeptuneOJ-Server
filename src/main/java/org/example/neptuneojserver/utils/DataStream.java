package org.example.neptuneojserver.utils;

import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;

@Component
public class DataStream {


    public String readStream(java.io.InputStream stream) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        StringBuilder output = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            output.append(line).append("\n");
        }
        return output.toString();
    }

    public float parseRealTime(String timeOutput) {
        String[] lines = timeOutput.split("\n");
        for (String line : lines) {
            if (line.startsWith("real")) {
                // Tách lấy giá trị 'real' (ví dụ: real    0m0.004s)
                String[] parts = line.split("\\s+");
                if (parts.length >= 2) {
                    String timeStr = parts[1].trim(); // Lấy phần tử thứ hai và loại bỏ khoảng trắng
                    return convertTimeToSeconds(timeStr); // Chuyển đổi thành giây và trả về
                }
            }
        }
        return -1; // Trường hợp không tìm thấy giá trị 'real'
    }

    public float convertTimeToSeconds(String timeStr) {
        try {
            // Kiểm tra đơn vị thời gian (m, s) và tính toán
            float seconds = 0;
            if (timeStr.contains("m")) {
                String[] parts = timeStr.split("m");
                float minutes = Float.parseFloat(parts[0]);
                float secondsPart = Float.parseFloat(parts[1].replace("s", ""));
                seconds = minutes * 60 + secondsPart;
            } else if (timeStr.contains("s")) {
                seconds = Float.parseFloat(timeStr.replace("s", ""));
            }
            return seconds;
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return -1; // Xảy ra lỗi chuyển đổi
        }
    }
}
