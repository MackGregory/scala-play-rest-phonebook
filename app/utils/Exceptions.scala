package utils

class PhoneNotFound extends Exception
class PhoneExists extends Exception
class InvalidPhoneNumber(message: String = "Invalid phone number format") extends Exception(message)