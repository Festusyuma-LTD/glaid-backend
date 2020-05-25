package festusyuma.com.glaid.service

import com.twilio.Twilio
import com.twilio.rest.api.v2010.account.Message
import com.twilio.type.PhoneNumber
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class TwilioService {

    @Value("\${TWILIO_ACCOUNT_SSID}")
    lateinit var accountSSID: String

    @Value("\${TWILIO_AUTH_TOKEN}")
    lateinit var authToken: String

    @Value("\${TWILIO_PHONE_NUMBER}")
    lateinit var phoneNumber: String

    fun sendSMS(sms: String, to: String) {
        Twilio.init(accountSSID, authToken)
        Message.creator(
                PhoneNumber(to),
                PhoneNumber(phoneNumber),
                sms
        ).create()
    }
}