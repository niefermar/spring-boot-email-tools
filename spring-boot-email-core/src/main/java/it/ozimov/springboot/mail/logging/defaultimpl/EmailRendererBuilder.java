package it.ozimov.springboot.mail.logging.defaultimpl;


import it.ozimov.springboot.mail.model.Email;
import it.ozimov.springboot.mail.model.EmailAttachment;
import lombok.NonNull;

import javax.mail.internet.InternetAddress;
import java.util.Collection;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.function.Function;
import java.util.function.UnaryOperator;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

public class EmailRendererBuilder {

    private static final Collector<CharSequence, ?, String> LIST_JOINER = Collectors.joining(", ", "[", "]");

    private final Email email;

    private Function<InternetAddress, String> fromFormat;
    private Function<InternetAddress, String> replyToFormat;
    private Function<InternetAddress, String> toFormat;
    private Function<InternetAddress, String> ccFormat;
    private Function<InternetAddress, String> bccFormat;
    private UnaryOperator<String> subjectFormat;
    private UnaryOperator<String> bodyFormat;
    private UnaryOperator<String> attachmentsFormat;
    private UnaryOperator<String> encodingFormat;
    private Function<Locale, String> localeFormat;
    private Function<Date, String> sentAtFormat;
    private Function<InternetAddress, String> receiptToFormat;
    private Function<InternetAddress, String> depositionNotificationToFormat;
    private boolean customHeadersIncluded;
    private boolean ignoreNullandEmptyCollections;

    public static EmailRendererBuilder builderFor(@NonNull final Email email) {
        return new EmailRendererBuilder(email);
    }

    private EmailRendererBuilder(@NonNull final Email email) {
        this.email = email;
    }

    public EmailRendererBuilder withFromFormat(@NonNull final Function<InternetAddress, String> fromFormat) {
        this.fromFormat = fromFormat;
        return this;
    }

    public EmailRendererBuilder withReplyToFormat(@NonNull final Function<InternetAddress, String> replyToFormat) {
        this.replyToFormat = replyToFormat;
        return this;
    }

    public EmailRendererBuilder withToFormat(@NonNull final Function<InternetAddress, String> toFormat) {
        this.toFormat = toFormat;
        return this;
    }

    public EmailRendererBuilder withCcFormat(@NonNull final Function<InternetAddress, String> ccFormat) {
        this.ccFormat = ccFormat;
        return this;
    }

    public EmailRendererBuilder withBccFormat(@NonNull final Function<InternetAddress, String> bccFormat) {
        this.bccFormat = bccFormat;
        return this;
    }

    public EmailRendererBuilder withSubjectFormat(@NonNull final UnaryOperator<String> subjectFormat) {
        this.subjectFormat = subjectFormat;
        return this;
    }

    public EmailRendererBuilder withBodyFormat(@NonNull final UnaryOperator<String> bodyFormat) {
        this.bodyFormat = bodyFormat;
        return this;
    }

    public EmailRendererBuilder withAttachmentsFormat(@NonNull final UnaryOperator<String> attachmentsFormat) {
        this.attachmentsFormat = attachmentsFormat;
        return this;
    }

    public EmailRendererBuilder withEncodingFormat(@NonNull final UnaryOperator<String> encodingFormat) {
        this.encodingFormat = encodingFormat;
        return this;
    }

    public EmailRendererBuilder withLocaleFormat(@NonNull final Function<Locale, String> localeFormat) {
        this.localeFormat = localeFormat;
        return this;
    }

    public EmailRendererBuilder withSentAtFormat(@NonNull final Function<Date, String> sentAtFormat) {
        this.sentAtFormat = sentAtFormat;
        return this;
    }

    public EmailRendererBuilder withReceiptToFormat(@NonNull final Function<InternetAddress, String> receiptToFormat) {
        this.receiptToFormat = receiptToFormat;
        return this;
    }

    public EmailRendererBuilder withDepositionNotificationToFormat(@NonNull final Function<InternetAddress, String> depositionNotificationToFormat) {
        this.depositionNotificationToFormat = depositionNotificationToFormat;
        return this;
    }

    public EmailRendererBuilder includeCustomHeaders() {
        customHeadersIncluded = true;
        return this;
    }

    public EmailRendererBuilder ignoreNullAndEmptyCollections() {
        ignoreNullandEmptyCollections = true;
        return this;
    }


    public String build() {
        final StringBuilder emailStringBuilder = new StringBuilder("Email{");

        boolean isFirst = true;
        if (nonNull(fromFormat) &&! skipField(email.getFrom())) {
            isFirst = false;

            emailStringBuilder.append("from=").append(fromFormat.apply(email.getFrom()));
        }
        if (nonNull(replyToFormat) &&! skipField(email.getReplyTo())) {
            if(!isFirst) emailStringBuilder.append(", ");
            isFirst = false;

            emailStringBuilder.append("replyTo=").append(replyToFormat.apply(email.getReplyTo()));
        }
        if (nonNull(toFormat) &&! skipField(email.getTo())) {
            if(!isFirst) emailStringBuilder.append(", ");
            isFirst = false;

            emailStringBuilder.append("to=").append(joinAddresses(email.getTo(), toFormat));
        }
        if (nonNull(ccFormat) &&! skipField(email.getCc())) {
            if(!isFirst) emailStringBuilder.append(", ");
            isFirst = false;

            emailStringBuilder.append("cc=").append(joinAddresses(email.getCc(), ccFormat));
        }
        if (nonNull(bccFormat) &&! skipField(email.getBcc())) {
            if(!isFirst) emailStringBuilder.append(", ");
            isFirst = false;

            emailStringBuilder.append("bcc=").append(joinAddresses(email.getBcc(), bccFormat));
        }
        if (nonNull(subjectFormat) &&! skipField(email.getSubject())) {
            if(!isFirst) emailStringBuilder.append(", ");
            isFirst = false;

            emailStringBuilder.append("subject=").append(subjectFormat.apply(email.getSubject()));
        }
        if (nonNull(bodyFormat) &&! skipField(email.getBody())) {
            if(!isFirst) emailStringBuilder.append(", ");
            isFirst = false;

            emailStringBuilder.append("body=").append(bodyFormat.apply(email.getBody()));
        }
        if (nonNull(attachmentsFormat) &&! skipField(email.getAttachments())) {
            if(!isFirst) emailStringBuilder.append(", ");
            isFirst = false;

            emailStringBuilder.append("attachments=").append(
                    email.getAttachments().stream()
                            .map(EmailAttachment::getAttachmentName).map(attachmentsFormat).collect(LIST_JOINER));
        }
        if (nonNull(encodingFormat) &&! skipField(email.getEncoding())) {
            if(!isFirst) emailStringBuilder.append(", ");
            isFirst = false;

            emailStringBuilder.append("encoding=").append(encodingFormat.apply(email.getEncoding()));
        }
        if (nonNull(localeFormat) &&! skipField(email.getLocale())) {
            if(!isFirst) emailStringBuilder.append(", ");
            isFirst = false;

            emailStringBuilder.append("locale=").append(localeFormat.apply(email.getLocale()));
        }
        if (nonNull(sentAtFormat) &&! skipField(email.getSentAt())) {
            if(!isFirst) emailStringBuilder.append(", ");
            isFirst = false;

            emailStringBuilder.append("sentAt=").append(sentAtFormat.apply(email.getSentAt()));
        }
        if (nonNull(receiptToFormat) &&! skipField(email.getReceiptTo())) {
            if(!isFirst) emailStringBuilder.append(", ");
            isFirst = false;

            emailStringBuilder.append("receiptTo=").append(receiptToFormat.apply(email.getReceiptTo()));
        }
        if (nonNull(depositionNotificationToFormat) &&! skipField(email.getDepositionNotificationTo())) {
            if(!isFirst) emailStringBuilder.append(", ");
            isFirst = false;

            emailStringBuilder.append("depositionNotificationTo=").append(depositionNotificationToFormat.apply(email.getDepositionNotificationTo()));
        }
        if (customHeadersIncluded &&! skipField(email.getCustomHeaders())) {
            if(!isFirst) emailStringBuilder.append(", ");

            emailStringBuilder.append("customHeaders=")
                    .append(email.getCustomHeaders().entrySet().stream()
                            .map(entry -> entry.getKey() + '=' + entry.getValue())
                            .collect(LIST_JOINER));
        }
        emailStringBuilder.append('}');
        return emailStringBuilder.toString();
    }

    private boolean skipField(Object object){
        return ignoreNullandEmptyCollections
                && (isNull(object)
                    || (object instanceof Collection && ((Collection) object).isEmpty())
                    || (object instanceof Map && ((Map) object).isEmpty())
        );
    }


    private String joinAddresses(final Collection<InternetAddress> internetAddressCollection, final Function<InternetAddress, String> internetAddressStringFormatter) {
        return internetAddressCollection.stream()
                .map(internetAddressStringFormatter)
                .collect(LIST_JOINER);
    }

}
