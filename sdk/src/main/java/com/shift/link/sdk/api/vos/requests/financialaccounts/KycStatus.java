package com.shift.link.sdk.api.vos.requests.financialaccounts;

public enum KycStatus {
    unknown,
    resubmit_details,
    upload_file,
    under_review,
    passed,
    rejected,
    temporary_error
}
