/*
 * Copyright © 2014-2015 Typesafe, Inc. All rights reserved. No information contained herein may be reproduced or
 * transmitted in any form or by any means without the express written permission of Typesafe, Inc.
 */

package com.typesafe.conductr.bundlelib;

class Common {
    private Common() {
    }

    static final String BUNDLE_ID = System.getenv("BUNDLE_ID");
    static final String USER_AGENT = "TypesafeConductRBundleLib";
}
