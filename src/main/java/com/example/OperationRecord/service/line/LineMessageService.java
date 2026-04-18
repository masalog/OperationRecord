package com.example.OperationRecord.service.line;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.springframework.stereotype.Service;

import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.model.PushMessage;
import com.linecorp.bot.model.action.Action;
import com.linecorp.bot.model.action.DatetimePickerAction;
import com.linecorp.bot.model.message.TemplateMessage;
import com.linecorp.bot.model.message.template.ButtonsTemplate;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LineMessageService {

    private final LineMessagingClient lineMessagingClient;

    public void sendDateTimePicker(String userId) {

        List<Action> actions = new ArrayList<>();

        // 日付（LocalDate）
        actions.add(
            DatetimePickerAction.OfLocalDate.builder()
                .label("日付を選ぶ")
                .data("action=selectDate")
                .initial(LocalDate.of(2026, 1, 1))
                .min(LocalDate.of(2026, 1, 1))
                .max(LocalDate.of(2030, 12, 31))
                .build()
        );

        // 時間（LocalTime）
        actions.add(
            DatetimePickerAction.OfLocalTime.builder()
                .label("時間を選ぶ")
                .data("action=selectTime")
                // 必要なら initial/min/max を LocalTime で指定
                .build()
        );

        // 日時（LocalDateTime）
        actions.add(
            DatetimePickerAction.OfLocalDatetime.builder()
                .label("日時を選ぶ")
                .data("action=selectDatetime")
                // 必要なら initial/min/max を LocalDateTime で指定
                .build()
        );

        ButtonsTemplate template = new ButtonsTemplate(
                null,
                "日時を選択してください",
                "以下から選んでください",
                actions
        );

        TemplateMessage message = new TemplateMessage("日時を選択してください", template);

        try {
            lineMessagingClient.pushMessage(new PushMessage(userId, message)).get();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("LINE pushMessage interrupted", e);
        } catch (ExecutionException e) {
            throw new RuntimeException("LINE pushMessage failed", e.getCause());
        }
    }
}