package com.ball.base.util;

import org.springframework.util.StringUtils;
import org.web3j.utils.Numeric;

/**
 * @author littlehow
 */
public class AddressUtil {
    public static boolean validEth(String address) {
        if (StringUtils.hasText(address) && address.length() == 42) {
            try {
                Numeric.toBigInt(address);
                return true;
            } catch (Exception e) {
                return false;
            }
        }
        return false;
    }
}
