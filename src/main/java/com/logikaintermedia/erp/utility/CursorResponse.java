package com.logikaintermedia.erp.utility;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class CursorResponse<T> {

    /**
     * Tujuan: Digunakan untuk Cursor-Based Pagination (Fitur Infinite Scroll).
     * Fungsi Utama: Menghindari data duplikat saat scrolling dan menjaga performa
     * database tetap cepat walaupun data sudah jutaan (lebih oke dibanding pakai
     * OFFSET).
     * Cara Kerja: Mengambil data terakhir dari list saat ini (last.getCreatedAt()
     * dan last.getId()) untuk dijadikan "penanda" (cursor) buat request data
     * selanjutnya.
     */
    private List<T> data;
    private OffsetDateTime nextCreatedAt;
    private UUID nextId;
    private boolean hasNext;

    public static <T extends HasCursor> CursorResponse<T> of(List<T> list) {

        CursorResponse<T> res = new CursorResponse<>();
        res.setData(list);

        if (list == null || list.isEmpty()) {
            res.setHasNext(false);
            return res;
        }

        T last = list.get(list.size() - 1);
        res.setNextCreatedAt(last.getCreatedAt());
        res.setNextId(last.getId());
        res.setHasNext(true);

        return res;
    }
}
