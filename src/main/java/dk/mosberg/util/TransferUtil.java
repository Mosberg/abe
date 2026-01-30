package dk.mosberg.util;

import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;

public final class TransferUtil {
    private TransferUtil() {}

    public static <T> long move(Storage<T> from, Storage<T> to, T variant, long maxAmount) {
        if (from == null || to == null)
            return 0;
        try (Transaction tx = Transaction.openOuter()) {
            long extracted = from.extract(variant, maxAmount, tx);
            if (extracted <= 0)
                return 0;
            long inserted = to.insert(variant, extracted, tx);
            if (inserted == extracted) {
                tx.commit();
                return inserted;
            }
        }
        return 0;
    }
}
