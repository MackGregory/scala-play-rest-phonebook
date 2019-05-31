package utils

class PhoneNotFound extends Exception
class PhoneExists extends Exception
class InvalidPhoneNumber(message: String = "Invalid phone number format") extends Exception(message)
class FailedToParseJson(message: String =
                        """Failed to parse Json. In request body Json should be: {"name": "name", "number": "number"}. Number format should be +xxxxxxxxxx where x is [0-9])""")
  extends Exception(message)