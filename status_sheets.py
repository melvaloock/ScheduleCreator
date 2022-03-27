from selenium import webdriver
from selenium.webdriver.support import expected_conditions as EC
from selenium.webdriver.common.by import By
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.common.desired_capabilities import DesiredCapabilities
import selenium.common.exceptions

import time


wait = None
thePage = "http://www2.gcc.edu/registrar/advising/2021.htm"
    
def verify(x):
    if not x:
        raise RuntimeError()

def open_browser(degree_audit_dir, headless=False):
    global wait
    
    # Open the browser
    options = webdriver.ChromeOptions()
    if headless:
        options.add_argument("--headless")
    options.add_argument('window-size=1920x1080')
    options.add_experimental_option("prefs", {
      "download.default_directory": degree_audit_dir,
      "download.prompt_for_download": False,
      "download.directory_upgrade": True,
      "safebrowsing.enabled": True,
      "plugins.always_open_pdf_externally": True
    })
    driver = webdriver.Chrome(chrome_options=options)
    wait = WebDriverWait(driver, 30)

    # Maximize window
    driver.maximize_window()
    
    return driver
    
driver = open_browser("status_sheets")
driver.get(thePage)

links = driver.find_elements_by_tag_name("a")

for lnk in links:
    lnk.click()
        
    time.sleep(2)
    
print("done")