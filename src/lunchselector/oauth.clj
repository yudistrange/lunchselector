(ns lunchselector.oauth
  (:require [ring.util.codec :as codec]
            [ring.util.response :as res]
            [bidi.ring :as bidi]
            [clj-http.client :as client]))

;; Google OAuth specific keys
(def client-id "220141946325-jeuee8ea9lo7al89tjgpfube1fnqibh8.apps.googleusercontent.com")
(def client-secret "6TVCYp3CYbQGFBppSR-a6iGe")
(def user (atom {:google-id "" :google-name "" :google-email ""}))
(def oauth-code-uri "https://accounts.google.com/o/oauth2/auth?")
(def oauth-token-uri "https://www.googleapis.com/oauth2/v4/token")
(def google-user-info-uri "https://www.googleapis.com/oauth2/v1/userinfo?")
(def redirect-uri "http://lunch.nilenso.com/login")

(def oauth-redirect (str oauth-code-uri
                         "scope=email%20profile&"
                         "redirect_uri=" (codec/url-encode redirect-uri) "&"
                         "response_type=code&"
                         "client_id=" (codec/url-encode client-id) "&"
                         "approval_prompt=force"))

(defn get-oauth-token [code]
  (client/post (str oauth-token-uri)
               {:form-params
                {:code  code
                 :client_id client-id
                 :client_secret client-secret
                 :redirect_uri redirect-uri
                 :grant_type "authorization_code"}}))

(defn get-user-details [token]
  (client/get (str google-user-info-uri
                   "access_token="
                   token)))