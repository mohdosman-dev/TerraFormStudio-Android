# Terra Form Studio — Requirements

## Purpose

A premium, mobile-first marketplace for handmade ceramics and pottery. Terra Form Studio is designed to feel less like a generic ecommerce app and more like a curated digital gallery where independent artisans can present their work with depth, craft, and story.

The product should balance two goals:
- deliver a high-end brand experience for collectors and interior-focused shoppers
- provide a storytelling-rich commerce platform for ceramic artists and potters

Initial scope focuses on discovery, artisan storytelling, product browsing, cart, and a unified 3-step checkout flow. Future scope includes a customer dashboard, wishlist features, editorial journal content, and advanced filtering.

## What Is a Product

A product is a handmade ceramic or pottery piece listed by an artisan for sale on the marketplace. Most items are unique or produced in very small quantities, so the product experience must emphasize individuality, material detail, and trust.

Each product has:
- a **title** (e.g. "Ash Glaze Serving Bowl", "Wheel-Thrown Stoneware Vase")
- an **artisan** associated with the piece
- a **primary image** and supporting gallery images
- a **price**
- a **material type** (e.g. stoneware, porcelain)
- a **technique** (e.g. wheel-thrown, hand-built)
- a **glaze or finish description**
- **dimensions** and other technical specifications
- an **availability state** such as available, low stock, sold, or made-to-order
- an optional **story or description** explaining process, inspiration, and care

### Product States

A product may appear in one of the following states:
- **Available** — can be added to cart and purchased immediately
- **Low Stock** — purchasable, but should signal scarcity and urgency carefully
- **Sold** — no longer available for purchase; may remain visible for storytelling or portfolio value
- **Made to Order** — available for purchase, but with production lead-time messaging

### Product Actions

Users can interact with a product in the following ways:
- browse it from curated home collections or artisan pages
- open the product detail page to review imagery, story, and specifications
- add it to cart
- proceed to checkout
- continue browsing after purchase

Future actions may include saving to wishlist, following artisans, and revisiting previously viewed pieces.

## Screens

### 1. Home Screen (Curated Discovery)

The home screen is the editorial front door of the marketplace. It should feel warm, refined, and highly curated rather than crowded or discount-driven.

**Layout:**
- Top area with brand mark, optional search entry point, and cart access
- Hero editorial section featuring a collection highlight, artisan spotlight, or seasonal curation
- Curated product modules such as **New Arrivals**, **Featured Artisans**, and **Collection Highlights**
- Story-led content blocks that mix imagery, short editorial copy, and product links
- Mobile-first vertical scroll with generous spacing and large imagery

**Content style:**
- strong photography-led presentation
- minimal but intentional copy
- emphasis on collection curation over dense merchandising grids
- premium tone with gallery-like pacing between sections

**Interactions:**
- Tap a featured product card to open the Product Detail Page
- Tap an artisan spotlight to open the Artisan Profile
- Tap cart icon to open Cart
- Tap any highlighted collection module to see related products

**Empty / fallback state:**
- If curated content is temporarily unavailable, show a minimal discovery layout with a small selection of featured products and a message such as: "New works are being curated now. Please check back soon."
- Cart access must remain available even if homepage editorial modules fail to load

### 2. Artisan Profile Screen

The artisan profile presents the maker as more than a seller. It should communicate philosophy, environment, process, and full collection in a way that deepens trust and emotional connection.

**Layout:**
- Top bar with back navigation and cart access
- Large artisan hero image or studio image
- Artisan name and short philosophy statement
- Narrative sections about studio practice, materials, influences, and process
- Grid or list of the artisan's available works
- Optional journal or story links related to the maker

**Content emphasis:**
- storytelling first, catalog second
- tactile, editorial presentation of studio atmosphere
- clear connection between artisan identity and listed works

**Interactions:**
- Tap a product from the artisan collection to open its Product Detail Page
- Tap back to return to the previous screen
- Tap cart icon to open Cart

**Empty state:**
- If the artisan has no active listings, show profile content normally with a message such as: "No available works at the moment. Explore the studio story and check back for future releases."

### 3. Product Detail Page (PDP)

The Product Detail Page is the core trust-building and conversion screen. It should combine tactile storytelling with precise technical detail.

**Layout:**
- Top image gallery with high-fidelity photography, optimized for mobile swipe and detail viewing
- Product title, artisan name, and price
- Short tactile description focused on form, glaze, texture, and intended use
- Technical specification block including dimensions, materials, glaze, and technique
- Availability or lead-time messaging
- Primary CTA to add to cart
- Supporting recommendations such as related works or "You May Also Like"

**Product gallery behavior:**
- high-quality imagery is required
- users should be able to understand scale, surface texture, glaze variation, and silhouette from the gallery
- image loading must remain performant on mobile devices

**Interactions:**
- Swipe or tap through product images
- Tap artisan name to open Artisan Profile
- Tap **Add to Cart** to place item in cart
- Tap a recommendation card to open another PDP

**Unavailable state:**
- If an item is sold, the primary CTA becomes unavailable and the screen should clearly communicate that the piece is no longer purchasable
- The product story and artisan link may remain visible for inspiration, archival value, or discovery of similar works

### 4. Cart Screen

The cart provides a clean visual summary of selected works while preserving the premium tone of the marketplace.

**Layout:**
- Top bar with back navigation and title
- List of selected products with thumbnail, title, artisan, price, and quantity or availability context where applicable
- Order subtotal summary
- Cross-sell section labeled similar to **You May Also Like** or **Curated Selection**
- Primary CTA to proceed to checkout

**Design intent:**
- simple and reassuring
- visually light, avoiding dense commerce clutter
- strong product imagery to remind users of the uniqueness of each piece

**Interactions:**
- Tap a cart item to revisit its Product Detail Page
- Remove an item from cart
- Tap the primary CTA to start checkout
- Tap a suggested item to open its PDP

**Empty state:**
- Show a calm, minimal state such as:
  - "Your cart is empty"
  - "Discover one-of-a-kind ceramics curated for modern living"
- Include a clear **Continue Shopping** action

### 5. Checkout — Step 1: Shipping

The first checkout step collects shipping details and delivery preferences in a way that feels secure and friction-light.

**Layout:**
- Step indicator showing progress through 3 steps
- Shipping address form fields
- Delivery method or shipping speed selection
- Order summary preview
- Primary CTA to continue to Payment

**Validation:**
- required shipping fields cannot be empty
- invalid address formatting should block progression
- delivery option must be selected when multiple options exist

**Interactions:**
- Enter address details
- Select delivery speed
- Continue to the next step

### 6. Checkout — Step 2: Payment

The payment step should feel secure, premium, and concise.

**Layout:**
- Step indicator with Shipping complete and Payment active
- Credit card entry form
- Express checkout options such as Apple Pay and PayPal
- Compact order summary
- Primary CTA to continue to Review

**Validation:**
- payment details must be complete and valid before continuing
- express checkout options should clearly indicate the selected method

**Interactions:**
- enter payment details
- choose express checkout when available
- continue to Review

### 7. Checkout — Step 3: Review

The review step gives the user a final chance to confirm all order details before completion.

**Layout:**
- Step indicator with Review active
- Shipping summary
- Payment summary
- Product summary with pricing
- Final total
- Primary CTA to place order

**Interactions:**
- review all details
- return to previous steps if editing is needed
- place order

**Error state:**
- if inventory or payment validation changes at the final step, clearly explain the issue and provide a path back to correction without losing entered information

### 8. Order Confirmation Screen

The confirmation screen is a celebratory success state that reassures the customer and encourages continued exploration.

**Layout:**
- success confirmation message
- order identifier
- delivery or tracking status area
- summary of purchased items
- clear **Continue Shopping** path

**Tone:**
- elegant and warm rather than overly playful
- celebratory, but still aligned with a premium gallery brand

**Interactions:**
- continue shopping from the confirmation screen
- access tracking when available

## Navigation

- **Home -> Product Detail**: tap any featured or listed product
- **Home -> Artisan Profile**: tap any artisan spotlight or maker module
- **Home -> Cart**: tap cart icon
- **Artisan Profile -> Product Detail**: tap an item in the artisan collection
- **Product Detail -> Artisan Profile**: tap artisan name or profile link
- **Product Detail -> Cart**: add item, then open cart
- **Cart -> Checkout Step 1**: tap checkout CTA
- **Checkout Step 1 -> Step 2**: complete shipping and continue
- **Checkout Step 2 -> Step 3**: complete payment and continue
- **Checkout Step 3 -> Confirmation**: place order successfully
- **Confirmation -> Home / Discovery**: tap continue shopping

The primary user journey should remain linear through checkout, with minimal distraction and clear progress feedback.

## Design System

The visual language should follow **The Tactile Gallery** design direction: premium, restrained, warm, and editorial.

### Colors

| Token | Hex | Usage |
|-------|-----|-------|
| Background | #F5F1EB | Main app background, soft gallery canvas |
| Surface | #FFFFFF | Cards, panels, elevated containers |
| Surface Warm | #EFE7DD | Section contrast, editorial modules |
| Primary | #8D775F | Core brand accent, CTAs, highlights |
| Primary Dark | #6F5D49 | Pressed state, stronger emphasis |
| Text Primary | #1F1A17 | Main headings and body copy |
| Text Secondary | #6E6258 | Secondary descriptions and metadata |
| Border | #D8CDC1 | Dividers, subtle outlines |
| Success | #4F7A5A | Confirmation, positive states |
| Error | #A35C4B | Validation and payment error states |

### Visual Direction

- mobile-first layout with refined spacing
- large-format photography and tactile product imagery
- restrained use of color, relying primarily on neutrals and taupe accents
- gallery-inspired composition rather than dense marketplace visuals
- premium whitespace and editorial pacing between sections

### Components

**Product Cards**
- image-first layout
- concise metadata
- artisan attribution visible
- premium, uncluttered styling

**Artisan Story Blocks**
- image and editorial copy combined
- warm backgrounds or neutral surfaces
- generous spacing and readable typography

**Checkout Components**
- simple form fields
- clear progress indicator
- trust-building hierarchy
- minimal visual noise

## Typography

The product should use **Noto Serif** as the primary brand typography direction to support a tactile, gallery-inspired identity.

| Style | Font | Weight | Size |
|-------|------|--------|------|
| Display Hero | Noto Serif | 700 | 32-40sp |
| Section Heading | Noto Serif | 600 | 24-28sp |
| Product Title | Noto Serif | 600 | 20-24sp |
| Body | Noto Serif / supporting sans if needed | 400 | 15-16sp |
| Label | Supporting sans-serif | 500 | 12-13sp |
| Button | Supporting sans-serif | 600 | 14-16sp |
| Price / Highlight | Noto Serif | 600 | 18-22sp |

Typography should feel elegant and readable on mobile devices, with strong hierarchy and minimal visual clutter.

**Please** refer to design in `specs/screen.html`

## Recommendations & Discovery Logic

### Curated Selection

Recommendation modules should feel editorial rather than algorithmically noisy. Related pieces should be chosen based on visual harmony, material, artisan relationship, or collection fit.

### You May Also Like

Cross-sell recommendations in cart and PDP should prioritize:
- complementary forms or color palettes
- similar material or glaze style
- pieces from the same artisan or related collection
- premium relevance over quantity

## Future Features

### Customer Dashboard

A future signed-in experience may include:
- order history
- saved artisans
- wishlist
- tracking access

### The Journal

A long-form editorial space for:
- care guides
- studio interviews
- behind-the-scenes kiln stories
- artisan process features

### Advanced Discovery

Future filters may include:
- material type such as stoneware or porcelain
- making technique such as hand-built or wheel-thrown
- glaze or finish type
- collection or artisan-based filtering

## Edge Cases

- **High image payloads**: optimize imagery for mobile using modern formats such as WebP or AVIF while preserving visual quality
- **Unique item inventory**: if an item sells out during checkout, clearly notify the user and prevent accidental purchase
- **Express checkout availability**: hide or disable unsupported payment methods cleanly
- **Empty recommendations**: omit recommendation modules rather than showing weak or repetitive suggestions
- **No artisan story content**: fall back to a concise profile summary instead of leaving large empty sections
- **Address or payment failure**: preserve user-entered data when validation fails
- **Slow network conditions**: prioritize image placeholders, progressive loading, and a stable layout
- **Tracking unavailable**: show order confirmation without live tracking, but preserve the order summary and continue shopping path

## Technical Considerations

- mobile-first responsive implementation
- high-quality image delivery with strong performance discipline
- SSL/TLS encryption for all transactions
- PCI-compliant payment processing for checkout
- consistent application of The Tactile Gallery design system across discovery, commerce, and post-purchase flows
