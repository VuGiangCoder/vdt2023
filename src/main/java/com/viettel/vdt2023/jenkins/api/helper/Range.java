package com.viettel.vdt2023.jenkins.api.helper;

public final class Range {

    /**
     * This represents {@code &#123;} (left curly bracket).
     */
    public static final String CURLY_BRACKET_OPEN = "%7B";
    /**
     * This represents {@code &#125;} (right curly bracket).
     */
    public static final String CURLY_BRACKET_CLOSE = "%7D";

    private Integer from;
    private Integer to;

    private Range() {
        this.from = null;
        this.to = null;
    }

    private Range setFrom(int from) {
        if (from < 0) {
            throw new IllegalArgumentException("from value must be greater or equal null.");
        }
        this.from = new Integer(from);
        return this;
    }

    private Range setTo(int to) {
        if (to < 0) {
            throw new IllegalArgumentException("to must be greater or equal null.");
        }
        this.to = new Integer(to);
        return this;
    }

    public String getRangeString() {
        StringBuilder sb = new StringBuilder();
        sb.append(CURLY_BRACKET_OPEN);
        if (this.from != null) {
            sb.append(String.format("%d", this.from));
        }

        sb.append(',');

        if (this.to != null) {
            sb.append(String.format("%d", this.to));
        }

        sb.append(CURLY_BRACKET_CLOSE);
        return sb.toString();
    }

    public static final class FromBuilder {
        private Range range;

        public FromBuilder(Range range) {
            this.range = range;
        }

        public Range to(int t) {
            this.range.setTo(t);
            if (range.to <= range.from) {
                throw new IllegalArgumentException("to must be greater than from");
            }
            return this.range;
        }

        public Range build() {
            return this.range;
        }
    }

    public static final class ToBuilder {
        private Range range;

        public ToBuilder(Range range) {
            this.range = range;
        }

        public Range build() {
            return this.range;
        }
    }

    public static final class Builder {
        private Range range;

        protected Builder() {
            this.range = new Range();
        }

        public FromBuilder from(int f) {
            this.range.setFrom(f);
            return new FromBuilder(this.range);
        }

        public ToBuilder to(int t) {
            this.range.setTo(t);
            return new ToBuilder(this.range);
        }

        public Range only(int only) {
            this.range.from = new Integer(only);
            this.range.to = new Integer(only + 1);
            return this.range;
        }
    }

    public static Builder build() {
        return new Builder();
    }
}