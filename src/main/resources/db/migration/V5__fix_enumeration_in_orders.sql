UPDATE store.orders SET status =
                            CASE
                                WHEN CAST(status AS SIGNED) = 0 THEN 'PENDING'
                                WHEN CAST(status AS SIGNED) = 1 THEN 'PAID'
                                WHEN CAST(status AS SIGNED) = 2 THEN 'FAILED'
                                WHEN CAST(status AS SIGNED) = 3 THEN 'CANCELLED'
                                ELSE 'PENDING' -- domyślna wartość
                                END;